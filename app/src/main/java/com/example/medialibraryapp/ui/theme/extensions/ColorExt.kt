package com.example.medialibraryapp.ui.theme.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.example.medialibraryapp.ui.theme.headingTextColorDark
import com.example.medialibraryapp.ui.theme.headingTextColorLight
import com.example.medialibraryapp.ui.theme.hyperlinkTextColorDark
import com.example.medialibraryapp.ui.theme.hyperlinkTextColorLight

val headingTextLarge: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) headingTextColorDark else headingTextColorLight

val hyperlinkTextColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) hyperlinkTextColorDark else hyperlinkTextColorLight