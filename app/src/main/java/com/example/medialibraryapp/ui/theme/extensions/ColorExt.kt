package com.example.medialibraryapp.ui.theme.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.example.medialibraryapp.ui.theme.backgroundColorDark
import com.example.medialibraryapp.ui.theme.backgroundColorLight
import com.example.medialibraryapp.ui.theme.headingTextColorDark
import com.example.medialibraryapp.ui.theme.headingTextColorLight
import com.example.medialibraryapp.ui.theme.hyperlinkTextColorDark
import com.example.medialibraryapp.ui.theme.hyperlinkTextColorLight
import com.example.medialibraryapp.ui.theme.imageDescriptionTextDark
import com.example.medialibraryapp.ui.theme.imageDescriptionTextLight

val headingTextLarge: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) headingTextColorDark else headingTextColorLight

val hyperlinkTextColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) hyperlinkTextColorDark else hyperlinkTextColorLight

val backgroundColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) backgroundColorDark else backgroundColorLight

val imageDescriptionTextColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) imageDescriptionTextDark else imageDescriptionTextLight