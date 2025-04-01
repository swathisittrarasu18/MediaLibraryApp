package com.example.medialibraryapp.screens.sign_in

import com.example.medialibraryapp.utils.core.TextFieldItem

data class SignInState(

    var emailAddress: TextFieldItem = TextFieldItem(),
    var password: TextFieldItem = TextFieldItem(),

    val isShowProgress: Boolean = false,
) {
    fun isValidationPassed(): Boolean = !emailAddress.isError && !password.isError
}
