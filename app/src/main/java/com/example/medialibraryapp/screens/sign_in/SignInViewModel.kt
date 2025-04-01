package com.example.medialibraryapp.screens.sign_in

import androidx.lifecycle.ViewModel
import com.example.data.extensions.validateString
import com.example.data.firebase.AuthRepository
import com.example.medialibraryapp.utils.composes.snackbar.showSnack
import com.example.medialibraryapp.utils.core.TextFieldItem
import com.example.medialibraryapp.utils.enums.SnackBarType
import com.example.medialibraryapp.utils.validations.SIGN_IN_FAILED
import com.example.medialibraryapp.utils.validations.SIGN_IN_SUCCESSFUL
import com.example.medialibraryapp.utils.validations.validateEmail
import com.example.medialibraryapp.utils.validations.validateSignInPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _state = MutableStateFlow(SignInState())
    val signInState = _state.asStateFlow()


    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.EmailChanged -> updateEmail(event.emailAddress)
            is SignInEvent.PasswordChanged -> updatePassword(event.password)

            is SignInEvent.EmailErrorChanged -> updateEmailError(event.isFocused)
            is SignInEvent.PasswordErrorChanged -> updatePasswordError(event.isFocused)
            is SignInEvent.SignInButtonClicked -> validation(event.onSuccess)
        }
    }

    private fun updateEmail(emailAddress: String?) {
        _state.update {
            it.copy(emailAddress = TextFieldItem(textValue = emailAddress.validateString()))
        }
    }

    private fun updatePassword(password: String?) {
        _state.update {
            it.copy(password = TextFieldItem(textValue = password.validateString()))
        }
    }

    private fun updateEmailError(isFocused: Boolean = false) {
        _state.update {
            val (isValidEmail, emailAddressError) = if (isFocused) Pair(true, "")
            else it.emailAddress.textValue.validateEmail()

            val emailAddress = it.emailAddress.copy(
                isError = !isValidEmail,
                errorMessage = emailAddressError
            )
            it.copy(emailAddress = emailAddress)
        }
    }

    private fun updatePasswordError(isFocused: Boolean = false) {
        _state.update {
            val (isValidPassword, passwordError) = if (isFocused) Pair(true, "")
            else it.password.textValue.validateSignInPassword()

            val password = it.password.copy(
                isError = !isValidPassword,
                errorMessage = passwordError
            )
            it.copy(password = password)
        }
    }

    private fun updateProgress(isShow: Boolean) {
        _state.update {
            it.copy(isShowProgress = isShow)
        }
    }

    private fun validation(onSuccess: () -> Unit) {
        updateEmailError()
        updatePasswordError()

        if (_state.value.isValidationPassed()) {
            signIn(
                email = _state.value.emailAddress.textValue,
                password = _state.value.password.textValue,
                onSuccess = onSuccess
            )
        }
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {

        updateProgress(true)
        authRepository.signInWithEmail(email, password) { success, errorMessage ->
            if (success) {
                onSuccess()
                showSnack(
                    message = SIGN_IN_SUCCESSFUL,
                    snackBarType = SnackBarType.SUCCESS
                )
            } else {
                showSnack(
                    message = errorMessage ?: SIGN_IN_FAILED,
                    snackBarType = SnackBarType.ERROR
                )
            }
        }
        updateProgress(false)
    }


}