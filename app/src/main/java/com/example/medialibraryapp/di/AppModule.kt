package com.example.medialibraryapp.di

import com.example.medialibraryapp.MainViewModel
import com.example.medialibraryapp.screens.media_detail_screen.MediaDetailViewModel
import com.example.medialibraryapp.screens.media_detail_screen.StorageManager1
import com.example.medialibraryapp.screens.media_gallery_screen.MediaGalleryViewModel
import com.example.medialibraryapp.screens.sign_in.SignInViewModel
import com.example.medialibraryapp.screens.sign_up.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { MediaDetailViewModel(get(), get(), get()) }
    single { StorageManager1(get()) }
    viewModel { MediaGalleryViewModel(get(), get()) }
    viewModel { MainViewModel() }


}