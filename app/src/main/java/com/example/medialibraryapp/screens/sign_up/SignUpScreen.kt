package com.example.medialibraryapp.screens.sign_up

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medialibraryapp.R
import com.example.medialibraryapp.screens.sign_in.ErrorMessageText
import com.example.medialibraryapp.ui.theme.extensions.backgroundColor
import com.example.medialibraryapp.ui.theme.extensions.headingTextLarge
import com.example.medialibraryapp.utils.composes.fillWidth
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    onNavigateTo: () -> Unit,
    onSuccess: () -> Unit
) {

    val viewModel: SignUpViewModel = koinViewModel()
    val state by viewModel.signUpState.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    SignUpScreenDesign(
        state = state,
        onEvent = onEvent,
        onNavigateTo = onNavigateTo,
        onSuccess = onSuccess
    )

}

@Preview(showSystemUi = true, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true, showBackground = true)
@Preview(device = Devices.PIXEL_TABLET, showSystemUi = true, showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_TABLET,
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun SignUpScreenDesignPreview() {
    SignUpScreenDesign(
        state = SignUpState(),
        onEvent = {},
        onNavigateTo = {},
        onSuccess = {}
    )
}

@Composable
private fun SignUpScreenDesign(
    state: SignUpState,
    onEvent: (SignUpEvent) -> Unit,
    onNavigateTo: () -> Unit,
    onSuccess: () -> Unit
) {

    var isFocused by remember { mutableStateOf(false) }
    var isInitial by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(backgroundColor),
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
                .fillWidth(0.3f)
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
                                onEvent(SignUpEvent.EmailErrorChanged(state.emailAddress.textValue.isBlank()))
                            }
                        },
                    value = state.emailAddress.textValue,
                    onValueChange = {
                        onEvent(SignUpEvent.EmailChanged(it))
                        if (!isInitial) { // Only check error after initial render
                            onEvent(SignUpEvent.EmailErrorChanged(it.isBlank()))
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
                                onEvent(SignUpEvent.PasswordErrorChanged(state.password.textValue.isBlank()))
                            }
                        },
                    value = state.password.textValue,
                    onValueChange = {
                        onEvent(SignUpEvent.PasswordChanged(it))
                        if (!isInitial) { // Only check error after initial render
                            onEvent(SignUpEvent.PasswordErrorChanged(it.isBlank()))
                        }
                    },
                    isError = state.password.isError
                )

                ErrorMessageText(
                    isError = state.password.isError,
                    errorMessage = state.password.errorMessage
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Text(
                    text = stringResource(R.string.confirm_password),
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
                                onEvent(SignUpEvent.ConfirmPasswordErrorChanged(state.confirmPassword.textValue.isBlank()))
                            }
                        },
                    value = state.confirmPassword.textValue,
                    onValueChange = {
                        onEvent(SignUpEvent.ConfirmPasswordChanged(it))
                        if (!isInitial) { // Only check error after initial render
                            onEvent(SignUpEvent.ConfirmPasswordErrorChanged(it.isBlank()))
                        }
                    },
                    isError = state.confirmPassword.isError
                )

                ErrorMessageText(
                    isError = state.confirmPassword.isError,
                    errorMessage = state.confirmPassword.errorMessage
                )
            }

            Button(
                onClick = { onEvent(SignUpEvent.SignUpButtonClicked(onSuccess)) },
                modifier = Modifier
            ) {
                Text(
                    text = stringResource(R.string.sign_up)
                )
            }

            TextButton(onClick = { onNavigateTo() }) {
                Text(stringResource(R.string.already_have_an_account_sign_in))
            }


        }
    }
}