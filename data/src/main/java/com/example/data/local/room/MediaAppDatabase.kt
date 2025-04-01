package com.example.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.room.dao.MediaDao
import com.example.data.local.room.entities.MediaEntity

@Database(entities = [MediaEntity::class], version = 1, exportSchema = false)
abstract class MediaAppDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao

//    companion object {
//        @Volatile
//        private var INSTANCE: MediaAppDatabase? = null
//
//        fun getDatabase(context: Context): MediaAppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    MediaAppDatabase::class.java,
//                    "media_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}
