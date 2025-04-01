package com.example.data.remote.respositories

import android.net.Uri
import com.example.data.local.room.dao.MediaDao
import com.example.data.local.room.entities.MediaEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class RemoteMediaRepository(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val mediaDao: MediaDao
) {

    suspend fun loadMediaFromRemote() {
        try {
            val snapshot = firestore.collection("media").get().await()
            val mediaList = snapshot.documents.mapNotNull { doc ->
                doc.data?.let {
                    MediaEntity(
                        id = doc.id,
                        name = it["name"] as? String ?: "",
                        url = it["url"] as? String ?: "",
                        type = it["type"] as? String ?: "",
                        size = (it["size"] as? Long) ?: 0L,
                        uploadDate = (it["uploadDate"] as? Long) ?: System.currentTimeMillis()
                    )
                }
            }
            mediaDao.insertMedia(mediaList)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun uploadMedia(uri: Uri, fileName: String, type: String) {
        val ref = storage.reference.child("media/$fileName")
        ref.putFile(uri).await()
        val downloadUrl = ref.downloadUrl.await().toString()

        val media = MediaEntity(
            id = UUID.randomUUID().toString(),
            name = fileName,
            url = downloadUrl,
            type = type,
            size = 0L,
            uploadDate = System.currentTimeMillis()
        )
        firestore.collection("media").document(fileName).set(media).await()
        mediaDao.insertMedia(listOf(media))
    }

    suspend fun deleteMedia(mediaId: String): Boolean {
        return try {
            val media = mediaDao.getMediaById(mediaId) ?: return false

            // Delete from Firestore
            firestore.collection("media").document(mediaId).delete().await().run {
                // Delete from Firebase Storage
                storage.getReferenceFromUrl(media.url).delete().await()
                mediaDao.deleteMedia(mediaId)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}