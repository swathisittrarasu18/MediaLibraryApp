package com.example.medialibraryapp

import androidx.lifecycle.ViewModel
import com.example.medialibraryapp.utils.composes.snackbar.SnackBarEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel() : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()


    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.UpdateSnackBarEvent -> updateSnackBarEvent(event.snackBarEvent)

        }
    }


    private fun updateSnackBarEvent(snackBarEvent: SnackBarEvent? = null) {
        _state.update {
            it.copy(snackBarEvent = snackBarEvent)
        }
    }

}
