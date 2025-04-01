package com.example.medialibraryapp.screens.media_detail_screen

import android.app.DownloadManager
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri


class StorageManager1(private val context: Context) {

    fun downloadFile(url: String, fileName: String): Boolean {
        return try {
            Log.d("DownloadDebug", "File URL: $url")
            val request = DownloadManager.Request(url.toUri())
                .setTitle(fileName)
                .setDescription("Downloading file")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setMimeType(getMimeType(fileName))  // Set the correct MIME type

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                // For Android 9 (Pie) and below, manually set destination
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            } else {
                // For Android 10+ (Q and above), let the system handle file location
                val uri = getMediaStoreUri(fileName)
                if (uri != null) {
                    request.setDestinationUri(uri)
                } else {
                    Log.e("StorageManager1", "Failed to get MediaStore URI for file: $fileName")
                    return false
                }
            }

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            true
        } catch (e: Exception) {
            Log.e("StorageManager1", "Download failed: ${e.message}", e)
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getMediaStoreUri(fileName: String): Uri? {
        val contentResolver = context.contentResolver
        val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        // Check if the file already exists
        val projection = arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DISPLAY_NAME)
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(fileName)

        contentResolver.query(collection, projection, selection, selectionArgs, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                return ContentUris.withAppendedId(collection, id) // Return existing URI
            }
        }

        // If file does not exist, insert a new entry
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(fileName))
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        return contentResolver.insert(collection, contentValues) // Insert new file
    }



    private fun getMimeType(fileName: String): String {
        return when {
            fileName.endsWith(".pdf", ignoreCase = true) -> "application/pdf"
            fileName.endsWith(".jpg", ignoreCase = true) || fileName.endsWith(".jpeg", ignoreCase = true) -> "image/jpeg"
            fileName.endsWith(".png", ignoreCase = true) -> "image/png"
            fileName.endsWith(".mp4", ignoreCase = true) -> "video/mp4"
            else -> "application/octet-stream"
        }
    }
}


