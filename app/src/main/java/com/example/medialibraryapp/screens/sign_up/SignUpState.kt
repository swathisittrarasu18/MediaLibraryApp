package com.example.medialibraryapp.screens.sign_up

import com.example.medialibraryapp.utils.core.TextFieldItem

data class SignUpState(

    var emailAddress: TextFieldItem = TextFieldItem(),
    var password: TextFieldItem = TextFieldItem(),
    var confirmPassword: TextFieldItem = TextFieldItem(),

    val isShowProgress: Boolean = false,
) {
    fun isValidationPassed(): Boolean =
        !emailAddress.isError && !password.isError && !confirmPassword.isError
}

