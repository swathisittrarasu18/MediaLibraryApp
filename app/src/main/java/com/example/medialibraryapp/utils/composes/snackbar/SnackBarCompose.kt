package com.example.medialibraryapp.utils.composes.snackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medialibraryapp.ui.theme.Blue
import com.example.medialibraryapp.ui.theme.Brown
import com.example.medialibraryapp.ui.theme.Red
import com.example.medialibraryapp.utils.enums.SnackBarType
import com.example.medialibraryapp.utils.sealed.UiText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun SnackBarComposeDesign(snackBarEvent: SnackBarEvent) {
    snackBarEvent.message?.asString()?.takeIf { it.isNotBlank() }?.let { message ->
        if (snackBarEvent.snackBarType != SnackBarType.NONE) {
            Snackbar(
                modifier = Modifier.padding(8.dp),
                containerColor = when (snackBarEvent.snackBarType) {
                    SnackBarType.SUCCESS -> Green
                    SnackBarType.ERROR -> Red
                    SnackBarType.WARNING -> Brown
                    SnackBarType.INFO -> Blue
                    else -> Color.Transparent
                }
            ) {
                Text(
                    text = message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


fun <T : ViewModel> T.showSnack(
    message: String?,
    snackBarType: SnackBarType = SnackBarType.NONE,
    snackBarAction: SnackBarAction? = null,
    isDismissAction: Boolean = true,
    isIndefinite: Boolean = false,
) {
    viewModelScope.launch {
        SnackBarController.sendEvent(
            event = SnackBarEvent(
                message = UiText.DynamicString(message),
                snackBarType = snackBarType,
                action = snackBarAction,
                isDismissAction = isDismissAction,
                isIndefinite = isIndefinite
            )
        )
    }
}

fun <T : ViewModel> T.showSnack(
    message: String?,
    snackBarAction: SnackBarAction? = null,
) {
    showSnack(
        message = message,
        snackBarType = SnackBarType.WARNING,
        snackBarAction = snackBarAction
    )
}

fun CoroutineScope.showSnack(
    message: String?,
    snackBarType: SnackBarType = SnackBarType.NONE,
    snackBarAction: SnackBarAction? = null,
    isDismissAction: Boolean = true,
    isIndefinite: Boolean = false,
) {
    launch {
        SnackBarController.sendEvent(
            event = SnackBarEvent(
                message = UiText.DynamicString(message),
                snackBarType = snackBarType,
                action = snackBarAction,
                isDismissAction = isDismissAction,
                isIndefinite = isIndefinite
            )
        )
    }
}

fun CoroutineScope.showSnack(
    message: String?,
    snackBarAction: SnackBarAction? = null,
) {
    showSnack(
        message = message,
        snackBarType = SnackBarType.WARNING,
        snackBarAction = snackBarAction
    )
}

fun CoroutineScope.showSnack(
    message: UiText?,
    snackBarType: SnackBarType = SnackBarType.NONE,
    snackBarAction: SnackBarAction? = null,
    isDismissAction: Boolean = true,
    isIndefinite: Boolean = false,
) {
    launch {
        SnackBarController.sendEvent(
            event = SnackBarEvent(
                message = message,
                snackBarType = snackBarType,
                action = snackBarAction,
                isDismissAction = isDismissAction,
                isIndefinite = isIndefinite
            )
        )
    }
}

fun CoroutineScope.showSnack(
    message: UiText?,
    snackBarAction: SnackBarAction? = null,
) {
    showSnack(
        message = message,
        snackBarType = SnackBarType.WARNING,
        snackBarAction = snackBarAction
    )
}