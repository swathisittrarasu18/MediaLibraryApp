package com.example.medialibraryapp.navigations

sealed class Screen(val route: String) {
   data object SignIn : Screen("signIn")
   data object SignUp : Screen("signUp")
   data object MediaGallery : Screen("mediaGallery")
   data object MediaDetail : Screen("mediaDetail/{mediaId}")
}
