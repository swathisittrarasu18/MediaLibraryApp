package com.example.data.local.room.repositories

import com.example.data.local.room.dao.MediaDao
import com.example.data.local.room.entities.MediaEntity
import kotlinx.coroutines.flow.Flow

class LocalMediaRepository(
    private val mediaDao: MediaDao
) {

    fun getMedia(): Flow<List<MediaEntity>> = mediaDao.getAllMedia()

    fun getMediaById(mediaId: String): MediaEntity? {
        return try {
            mediaDao.getMediaById(mediaId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteMediaById(mediaId: String): Boolean {
        return try {
            val rowsDeleted = mediaDao.deleteMedia(mediaId)
            rowsDeleted > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}

