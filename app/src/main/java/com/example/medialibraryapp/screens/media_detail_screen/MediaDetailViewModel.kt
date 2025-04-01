package com.example.medialibraryapp.screens.media_detail_screen


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.room.repositories.LocalMediaRepository
import com.example.data.remote.respositories.RemoteMediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaDetailViewModel(
    private val localMediaRepository: LocalMediaRepository,
    private val remoteMediaRepository: RemoteMediaRepository,
    private val storageManager: StorageManager1
) : ViewModel() {

    private val _state = MutableStateFlow(MediaDetailState())
    val state: StateFlow<MediaDetailState> = _state.asStateFlow()

    fun onEvent(event: MediaDetailEvent) {
        when (event) {
            is MediaDetailEvent.LoadMedia -> loadMedia(event.mediaId)
            MediaDetailEvent.DownloadMedia -> downloadMedia()
            MediaDetailEvent.DeleteMedia -> deleteMedia()
        }
    }

    private fun loadMedia(mediaId: String) {
        Log.d("MediaDetail", "Loading media with ID: $mediaId")
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)

            localMediaRepository.getMediaById(mediaId = mediaId)?.let { media ->
                _state.value = _state.value.copy(media = media, isLoading = false)
            } ?: run {
                _state.value = _state.value.copy(error = "Media not found", isLoading = false)
            }
        }
    }


    private fun downloadMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            state.value.media?.let { media ->
                val success = storageManager.downloadFile(media.url, media.name)
                if (!success) {
                    _state.value = _state.value.copy(error = "Download failed")
                }
            }
        }
    }

    private fun deleteMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            state.value.media?.let { media ->
                if (remoteMediaRepository.deleteMedia(media.id)) {
                    _state.value = _state.value.copy(isDeleted = true)
                } else {
                    _state.value = _state.value.copy(error = "Failed to delete media")
                }
            }
        }
    }
}
