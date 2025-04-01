package com.example.medialibraryapp

import com.example.medialibraryapp.utils.composes.snackbar.SnackBarEvent

sealed interface MainEvent {

    data class UpdateSnackBarEvent(val snackBarEvent: SnackBarEvent? = null) : MainEvent

}