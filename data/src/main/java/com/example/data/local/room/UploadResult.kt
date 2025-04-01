package com.example.data.local.room

sealed class UploadResult {
    object Success : UploadResult()
    data class Failure(val error: String) : UploadResult()
}