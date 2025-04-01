package com.example.medialibraryapp.utils.composes.snackbar

import androidx.compose.material3.SnackbarDuration
import com.example.data.extensions.isValidString
import com.example.medialibraryapp.utils.enums.SnackBarType
import com.example.medialibraryapp.utils.sealed.UiText

data class SnackBarEvent(
    val message: UiText?,
    val action: SnackBarAction? = null,
    val snackBarType: SnackBarType,
    val isDismissAction: Boolean = true,
    val isIndefinite: Boolean = false,
) {

    fun getSnackBarDuration(): SnackbarDuration = when {
        action?.name.isValidString() || isIndefinite -> SnackbarDuration.Indefinite
        else -> SnackbarDuration.Short
    }

}
