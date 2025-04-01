package com.example.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media")
data class MediaEntity(
    @PrimaryKey val id: String,
    val name: String,
    val url: String,
    val type: String,
    val size: Long,
    val uploadDate: Long
)
