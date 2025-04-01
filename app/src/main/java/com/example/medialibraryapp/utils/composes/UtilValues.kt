package com.example.medialibraryapp.utils.composes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController


@Composable
fun ProgressBarCompose(isShowProgress: Boolean) {
    if (isShowProgress) {
        LocalFocusManager.current.clearFocus()
        LocalSoftwareKeyboardController.current?.hide()

        Box(
            modifier = Modifier
                .fillMaxSize(), // Semi-transparent background
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
