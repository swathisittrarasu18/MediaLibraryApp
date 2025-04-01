package com.example.medialibraryapp.screens.sign_in

import com.example.medialibraryapp.utils.core.TextFieldItem

data class SignInState(

    var emailAddress: TextFieldItem = TextFieldItem("swathi+g@gmail.com"),
    var password: TextFieldItem = TextFieldItem("Swa@12345"),

    val isShowProgress: Boolean = false,
) {
    fun isValidationPassed(): Boolean = !emailAddress.isError && !password.isError
}
