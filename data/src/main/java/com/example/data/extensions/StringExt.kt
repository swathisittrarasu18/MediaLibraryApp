package com.example.data.extensions


fun CharSequence?.isValidString(): Boolean {

    if (isNullOrEmpty()) return false

    val textValue = toString()

    return textValue.trim { it <= ' ' }.isNotEmpty() &&

            !textValue.trim { it <= ' ' }.equals("null", ignoreCase = true) &&

            !textValue.trim { it <= ' ' }.equals("", ignoreCase = true)

}

fun String?.validateString(): String = if (isNullOrEmpty()) "" else trim()

