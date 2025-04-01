package com.example.medialibraryapp.utils.composes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.example.medialibraryapp.utils.composes.DeviceUtils.isTablet


object DeviceUtils {
    @Composable
    fun isTablet(): Boolean {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp >= 600
    }
}


@Composable
fun Modifier.fillWidth(widthFraction: Float = 0.3f): Modifier =
    if (isTablet()) fillMaxWidth(widthFraction) else fillMaxWidth()


