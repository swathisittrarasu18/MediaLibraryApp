package com.example.medialibraryapp.screens.media_gallery_screen

import android.net.Uri

sealed interface MediaGalleryEvent {
    data class UploadMedia(val uri: Uri, val fileName: String, val type: String) : MediaGalleryEvent

}