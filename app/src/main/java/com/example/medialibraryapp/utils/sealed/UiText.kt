package com.example.medialibraryapp.utils.sealed

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.data.extensions.validateString

sealed class UiText {

    data class DynamicString(val value: String? = "") : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : UiText()

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value.validateString()
        is StringResource -> stringResource(resId, *args)
    }

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value.validateString()
        is StringResource -> context.getString(resId, *args)
    }
}