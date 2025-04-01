package com.example.medialibraryapp.ui.theme.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.example.medialibraryapp.ui.theme.appBarColorDark
import com.example.medialibraryapp.ui.theme.appBarColorLight
import com.example.medialibraryapp.ui.theme.appBarTitleColorDark
import com.example.medialibraryapp.ui.theme.appBarTitleColorLight
import com.example.medialibraryapp.ui.theme.backgroundColorDark
import com.example.medialibraryapp.ui.theme.backgroundColorLight
import com.example.medialibraryapp.ui.theme.headingTextColorDark
import com.example.medialibraryapp.ui.theme.headingTextColorLight
import com.example.medialibraryapp.ui.theme.imageDescriptionTextDark
import com.example.medialibraryapp.ui.theme.imageDescriptionTextLight

val headingTextLarge: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) headingTextColorDark else headingTextColorLight


val appBarColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) appBarColorDark else appBarColorLight

val appBarTitleColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) appBarTitleColorDark else appBarTitleColorLight

val backgroundColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) backgroundColorDark else backgroundColorLight

val imageDescriptionTextColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) imageDescriptionTextDark else imageDescriptionTextLight