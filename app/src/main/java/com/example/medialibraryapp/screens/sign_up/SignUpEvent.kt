package com.example.medialibraryapp.screens.sign_up

sealed interface SignUpEvent {
    data class EmailChanged(val emailAddress: String?) : SignUpEvent
    data class PasswordChanged(val password: String?) : SignUpEvent
    data class ConfirmPasswordChanged(val confirmPassword: String?) : SignUpEvent

    data class EmailErrorChanged(val isFocused: Boolean = false) : SignUpEvent
    data class PasswordErrorChanged(val isFocused: Boolean = false) : SignUpEvent
    data class ConfirmPasswordErrorChanged(val isFocused: Boolean = false) : SignUpEvent

    data class SignUpButtonClicked(val onSuccess: () -> Unit) : SignUpEvent
}