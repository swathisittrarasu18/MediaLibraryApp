package com.example.medialibraryapp

import com.example.medialibraryapp.utils.composes.snackbar.SnackBarEvent
import com.example.medialibraryapp.utils.enums.SnackBarType
import com.example.medialibraryapp.utils.sealed.UiText

data class MainState(
    val snackBarEvent: SnackBarEvent? = SnackBarEvent(
        message = UiText.StringResource(R.string.empty),
        snackBarType = SnackBarType.NONE
    )
)
