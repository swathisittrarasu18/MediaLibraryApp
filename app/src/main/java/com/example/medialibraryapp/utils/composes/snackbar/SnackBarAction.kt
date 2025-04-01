package com.example.medialibraryapp.utils.composes.snackbar

data class SnackBarAction(
    val name: String,
    val action: () -> Unit,
)
