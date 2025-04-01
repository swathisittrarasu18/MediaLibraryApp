package com.example.medialibraryapp.screens.media_detail_screen

import com.example.data.local.room.entities.MediaEntity

data class MediaDetailState(
    val media: MediaEntity? = null,
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false,
    val error: String? = null,
    val isShowDeleteConfirmationDialog: Boolean = false,
)
