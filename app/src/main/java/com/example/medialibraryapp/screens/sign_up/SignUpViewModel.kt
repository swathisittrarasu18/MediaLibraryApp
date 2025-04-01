package com.example.medialibraryapp.screens.sign_up

import androidx.lifecycle.ViewModel
import com.example.data.extensions.validateString
import com.example.data.firebase.AuthRepository
import com.example.medialibraryapp.utils.composes.snackbar.showSnack
import com.example.medialibraryapp.utils.core.TextFieldItem
import com.example.medialibraryapp.utils.enums.SnackBarType
import com.example.medialibraryapp.utils.validations.SIGN_UP_FAILED
import com.example.medialibraryapp.utils.validations.SIGN_UP_SUCCESSFUL
import com.example.medialibraryapp.utils.validations.validateConfirmPassword
import com.example.medialibraryapp.utils.validations.validateEmail
import com.example.medialibraryapp.utils.validations.validateSignUpPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val signUpState = _state.asStateFlow()


    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EmailChanged -> updateEmail(event.emailAddress)
            is SignUpEvent.PasswordChanged -> updatePassword(event.password)
            is SignUpEvent.ConfirmPasswordChanged -> updateConfirmPassword(event.confirmPassword)


            is SignUpEvent.ConfirmPasswordErrorChanged -> updateConfirmPasswordError(event.isFocused)
            is SignUpEvent.EmailErrorChanged -> updateEmailError(event.isFocused)
            is SignUpEvent.PasswordErrorChanged -> updatePasswordError(event.isFocused)
            is SignUpEvent.SignUpButtonClicked -> validation(event.onSuccess)
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

    private fun updateConfirmPassword(password: String?) {
        _state.update {
            it.copy(confirmPassword = TextFieldItem(textValue = password.validateString()))
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
            else it.password.textValue.validateSignUpPassword()

            val password = it.password.copy(
                isError = !isValidPassword,
                errorMessage = passwordError
            )
            it.copy(password = password)
        }
    }

    private fun updateConfirmPasswordError(isFocused: Boolean = false) {
        _state.update {
            val (isValidConfirmPassword, confirmPasswordError) = if (isFocused) Pair(true, "")
            else it.confirmPassword.textValue.validateConfirmPassword(it.password.textValue)

            val confirmPassword = it.confirmPassword.copy(
                isError = !isValidConfirmPassword,
                errorMessage = confirmPasswordError
            )
            it.copy(confirmPassword = confirmPassword)
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
        updateConfirmPasswordError()

        if (_state.value.isValidationPassed()) {
            signUp(
                email = _state.value.emailAddress.textValue,
                password = _state.value.password.textValue,
                onSuccess = onSuccess
            )
        }
    }

    fun signUp(email: String, password: String, onSuccess: () -> Unit) {
        updateProgress(true)
        authRepository.signUpWithEmail(email, password) { success, errorMessage ->
            if (success) {
                onSuccess()
                showSnack(
                    message = SIGN_UP_SUCCESSFUL,
                    snackBarType = SnackBarType.SUCCESS
                )
            } else {
                showSnack(
                    message = errorMessage ?: SIGN_UP_FAILED,
                    snackBarType = SnackBarType.ERROR
                )
            }
        }
        updateProgress(false)
    }
}