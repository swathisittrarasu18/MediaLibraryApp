package com.example.medialibraryapp.screens.sign_in

sealed interface SignInEvent {
    data class EmailChanged(val emailAddress: String?) : SignInEvent
    data class PasswordChanged(val password: String?) : SignInEvent

    data class EmailErrorChanged(val isFocused: Boolean = false) : SignInEvent
    data class PasswordErrorChanged(val isFocused: Boolean = false) : SignInEvent

    data class SignInButtonClicked(val onSuccess: () -> Unit) : SignInEvent


}