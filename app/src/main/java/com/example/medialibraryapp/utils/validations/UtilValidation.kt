package com.example.medialibraryapp.utils.validations

import com.example.data.extensions.validateString

fun String?.isHaveSpecialCharacters(): Boolean {
    validateString().forEach {
        if (!it.isLetterOrDigit()) return true
    }
    return false
}