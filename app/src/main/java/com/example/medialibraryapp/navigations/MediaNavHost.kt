package com.example.medialibraryapp.navigations

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.extensions.validateString
import com.example.medialibraryapp.MainEvent
import com.example.medialibraryapp.MainViewModel
import com.example.medialibraryapp.screens.media_detail_screen.MediaDetailScreen
import com.example.medialibraryapp.screens.media_gallery_screen.MediaGalleryScreen
import com.example.medialibraryapp.screens.sign_in.SignInScreen
import com.example.medialibraryapp.screens.sign_up.SignUpScreen
import com.example.medialibraryapp.utils.composes.ObserveAsEvents
import com.example.medialibraryapp.utils.composes.snackbar.SnackBarComposeDesign
import com.example.medialibraryapp.utils.composes.snackbar.SnackBarController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun MediaAppNavHost(navController: NavHostController = rememberNavController()) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    val mainViewModel: MainViewModel = koinViewModel()
    val state by mainViewModel.state.collectAsStateWithLifecycle()
    val onEvent = mainViewModel::onEvent

    ObserveAsEvents(
        flow = SnackBarController.events,
        key1 = snackBarHostState,
    ) { updatedSnackBarEvent ->
        onEvent(MainEvent.UpdateSnackBarEvent(updatedSnackBarEvent))
        scope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()
            state.snackBarEvent?.let { newSnackBarEvent ->
                snackBarHostState.showSnackbar(
                    message = newSnackBarEvent.message?.asString(context).validateString(),
                    actionLabel = newSnackBarEvent.action?.name,
                    duration = newSnackBarEvent.getSnackBarDuration()
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { snackBarData ->
                    state.snackBarEvent?.let { newSnackBarEvent ->
                        SnackBarComposeDesign(snackBarEvent = newSnackBarEvent)
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->


        NavHost(
            modifier = Modifier
                .padding(innerPadding),
            navController = navController, startDestination = Screen.SignIn.route
        ) {

            composable(Screen.SignIn.route) {
                SignInScreen(
                    onNavigateTo = { navController.navigate(Screen.SignUp.route) },
                    onSuccess = { navController.navigate(Screen.MediaGallery.route) }
                )
            }

            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onNavigateTo = { navController.popBackStack() }, // Go back to SignIn
                    onSuccess = { navController.navigate(Screen.MediaGallery.route) }
                )
            }

            composable(Screen.MediaGallery.route) {
                MediaGalleryScreen(
                    onMediaClick = { media ->
                        navController.navigate(Screen.MediaDetail.route + "/${media.id}")
                    }
                )
            }

            composable(Screen.MediaDetail.route + "/{mediaId}") { backStackEntry ->
                val mediaId = backStackEntry.arguments?.getString("mediaId") ?: ""
                MediaDetailScreen(mediaId = mediaId) {
                    navController.popBackStack()
                }
            }
        }
    }
}
