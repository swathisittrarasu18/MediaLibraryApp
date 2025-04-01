package com.example.medialibraryapp.screens.media_gallery_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.room.repositories.LocalMediaRepository
import com.example.data.remote.respositories.RemoteMediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MediaGalleryViewModel(
    private val localRepository: LocalMediaRepository,
    private val remoteRepository: RemoteMediaRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MediaGalleryState())
    val state: StateFlow<MediaGalleryState> = _state.asStateFlow()

    init {
//        loadMediaFromRemote()
        loadMediaFromLocal()
    }

    fun onEvent(event: MediaGalleryEvent) {
        when (event) {
            is MediaGalleryEvent.UploadMedia -> uploadMedia(
                event.uri,
                event.fileName,
                event.type,
                event.size
            )
        }
    }

    private fun loadMediaFromLocal() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            localRepository.getMedia().collect { mediaList ->
                _state.value = _state.value.copy(mediaList = mediaList, isLoading = false)
            }
        }
    }

    private fun loadMediaFromRemote() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            remoteRepository.loadMediaFromRemote()
            _state.value = _state.value.copy(isLoading = false)
        }
    }


    private fun uploadMedia(uri: Uri, fileName: String, type: String, size: Long) {
        updateProgress(true)
        viewModelScope.launch {
            remoteRepository.uploadMedia(uri, fileName, type, size)
            loadMediaFromLocal()
        }
        updateProgress(false)
    }

    private fun updateProgress(isLoading: Boolean) {
        _state.update {
            it.copy(isLoading = isLoading)
        }
    }


}
