package com.example.medialibraryapp.screens.sign_in


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.extensions.isValidString
import com.example.data.extensions.validateString
import com.example.medialibraryapp.R
import com.example.medialibraryapp.ui.theme.extensions.headingTextLarge
import org.koin.androidx.compose.koinViewModel


@Composable
fun SignInScreen(
    onNavigateTo: () -> Unit,
    onSuccess: () -> Unit
) {

    val viewModel: SignInViewModel = koinViewModel()
    val state by viewModel.signInState.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    SignInMobileScreenDesign(
        state = state,
        onEvent = onEvent,
        onNavigateTo = onNavigateTo,
        onSuccess = onSuccess
    )


}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignInScreenDesignPreview() {
    SignInMobileScreenDesign(
        state = SignInState(),
        onEvent = {},
        onNavigateTo = {},
        onSuccess = {}
    )
}

@Composable
private fun SignInMobileScreenDesign(
    state: SignInState,
    onEvent: (SignInEvent) -> Unit,
    onNavigateTo: () -> Unit,
    onSuccess: () -> Unit
) {

    var isFocused by remember { mutableStateOf(false) }
    var isInitial by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (state.isShowProgress) {
            Box(
                modifier = Modifier
                    .fillMaxSize(), // Semi-transparent background
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),

            ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Text(
                    text = stringResource(R.string.email_address),
                    style = MaterialTheme.typography.titleSmall.copy(),
                    color = headingTextLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                            if (!focusState.isFocused) {
                                onEvent(SignInEvent.EmailErrorChanged(state.emailAddress.textValue.isBlank()))
                            }
                        },
                    value = state.emailAddress.textValue,
                    onValueChange = {
                        onEvent(SignInEvent.EmailChanged(it))
                        if (!isInitial) { // Only check error after initial render
                            onEvent(SignInEvent.EmailErrorChanged(it.isBlank()))
                        }
                    },
                    isError = state.emailAddress.isError
                )

                ErrorMessageText(
                    isError = state.emailAddress.isError,
                    errorMessage = state.emailAddress.errorMessage
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Text(
                    text = stringResource(R.string.password),
                    style = MaterialTheme.typography.titleSmall.copy(),
                    color = headingTextLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                            if (!focusState.isFocused) {
                                onEvent(SignInEvent.PasswordErrorChanged(state.password.textValue.isBlank()))
                            }
                        },
                    value = state.password.textValue,
                    onValueChange = {
                        onEvent(SignInEvent.PasswordChanged(it))
                        if (!isInitial) { // Only check error after initial render
                            onEvent(SignInEvent.PasswordErrorChanged(it.isBlank()))
                        }
                    },
                    isError = state.password.isError
                )

                ErrorMessageText(
                    isError = state.password.isError,
                    errorMessage = state.password.errorMessage
                )
            }

            Button(
                onClick = { onEvent(SignInEvent.SignInButtonClicked(onSuccess)) },
                modifier = Modifier
            ) {
                Text(
                    text = stringResource(R.string.sign_in)
                )
            }

            TextButton(onClick = { onNavigateTo() }) {
                Text(stringResource(R.string.don_t_have_an_account_sign_up))
            }


        }

    }


}

@Composable
fun ErrorMessageText(
    isError: Boolean,
    errorMessage: String?,
    bottomPadding: Dp = 0.dp,
    startPadding: Dp = 5.dp,
) {
    AnimatedVisibility(isError && errorMessage.isValidString()) {
        Text(
            text = errorMessage.validateString(),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(
                start = startPadding,
                bottom = bottomPadding
            )
        )
    }
}


