package com.example.data.di


import androidx.room.Room
import com.example.data.firebase.AuthRepository
import com.example.data.local.room.MediaAppDatabase
import com.example.data.local.room.repositories.LocalMediaRepository
import com.example.data.remote.respositories.RemoteMediaRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val dataModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { AuthRepository(get()) }
    single { RemoteMediaRepository(get(), get(), get()) }
    single { LocalMediaRepository( get()) }


    // Provide MediaDao
    single { get<MediaAppDatabase>().mediaDao() }

    // Provide MediaAppDatabase
    single {
        Room.databaseBuilder(
            androidContext(),
            MediaAppDatabase::class.java,
            "media_database"
        ).build()
    }
}