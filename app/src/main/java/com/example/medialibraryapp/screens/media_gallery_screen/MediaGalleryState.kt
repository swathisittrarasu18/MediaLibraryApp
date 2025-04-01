package com.example.medialibraryapp.screens.media_gallery_screen

import com.example.data.local.room.entities.MediaEntity

data class MediaGalleryState(
    val mediaList: List<MediaEntity> = emptyList(),
    val isLoading: Boolean = false
)
