package com.example.medialibraryapp.screens.media_detail_screen

sealed interface MediaDetailEvent {
    data class LoadMedia(val mediaId: String) : MediaDetailEvent
    object DownloadMedia : MediaDetailEvent
    object DeleteMedia : MediaDetailEvent
}