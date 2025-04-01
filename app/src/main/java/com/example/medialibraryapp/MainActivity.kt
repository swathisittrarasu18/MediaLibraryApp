package com.example.medialibraryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.medialibraryapp.navigations.MediaAppNavHost
import com.example.medialibraryapp.ui.theme.MediaLibraryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediaLibraryAppTheme {
                MediaAppNavHost()
            }
        }
    }
}

