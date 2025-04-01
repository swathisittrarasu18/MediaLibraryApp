package com.example.medialibraryapp.screens.media_detail_screen

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri


class StorageManager1(private val context: Context) {

    fun downloadFile(fileUrl: String, fileName: String): Boolean {
        if (fileUrl.isEmpty()) {
            Log.e("DownloadError", "File URL is empty")
            return false
        }

        try {
            val request = DownloadManager.Request(fileUrl.toUri())
                .setTitle("Downloading File")
                .setDescription("Downloading $fileName")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            Log.d("DownloadSuccess", "Download started for $fileName")
            return true
        } catch (e: Exception) {
            Log.e("DownloadError", "Failed to enqueue download: ${e.message}")
            return false
        }
    }
}


