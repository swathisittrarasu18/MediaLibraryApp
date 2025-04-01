package com.example.medialibraryapp.screens.media_detail_screen

sealed interface MediaDetailEvent {
    data class LoadMedia(val mediaId: String) : MediaDetailEvent
    data object DownloadMedia : MediaDetailEvent
    data object DeleteMedia : MediaDetailEvent
    data class DismissConfirmationDialog(val isShow: Boolean) : MediaDetailEvent
}