package com.example.medialibraryapp.utils.validations

import com.example.data.extensions.validateString

val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

fun String?.validateEmail(): Pair<Boolean, String> = when {
    isNullOrEmpty() -> Pair(false, EMAIL_ADDRESS_REQUIRED)
    !validateString().matches(EMAIL_REGEX) -> Pair(false, ENTER_VALID_EMAIL_ADDRESS)
    else -> Pair(true, "")
}

fun String?.validateSignInPassword(): Pair<Boolean, String> = when {
    isNullOrEmpty() -> Pair(false, PASSWORD_REQUIRED)
    validateString().length < 8 -> Pair(false, INVALID_PASSWORD)
    else -> Pair(true, "")
}

fun String?.validateSignUpPassword(): Pair<Boolean, String> =
    when {
        isNullOrEmpty() -> Pair(false, PASSWORD_REQUIRED)

        validateString().length !in 8..20 -> Pair(false, PLEASE_ENTER_MINIMUM_8_CHARACTERS)

        !validateString().any { it.isUpperCase() } -> Pair(
            false, PASSWORD_MUST_CONTAIN_AT_LEAST_ONE_UPPERCASE_LETTER
        )

        !validateString().any { it.isLowerCase() } -> Pair(
            false, PASSWORD_MUST_CONTAIN_AT_LEAST_ONE_LOWERCASE_LETTER
        )

        !validateString().any { it.isDigit() } -> Pair(
            false,
            PASSWORD_MUST_CONTAIN_AT_LEAST_ONE_NUMBER
        )

        !isHaveSpecialCharacters() -> Pair(
            false, PASSWORD_MUST_CONTAIN_AT_LEAST_ONE_SPECIAL_CHARACTER
        )

        else -> Pair(true, "")
    }

fun String?.validateConfirmPassword(password: String?): Pair<Boolean, String> = when {
    isNullOrEmpty() -> Pair(false, CONFIRM_PASSWORD_REQUIRED)
    validateString() != password -> Pair(false, PASSWORDS_DO_NOT_MATCH)
    else -> Pair(true, "")
}