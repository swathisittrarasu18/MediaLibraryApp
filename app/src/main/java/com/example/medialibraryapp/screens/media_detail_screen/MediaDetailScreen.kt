package com.example.medialibraryapp.screens.media_detail_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaDetailScreen(
    mediaId: String,
    onBack: () -> Unit
) {

    val viewModel: MediaDetailViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    MediaDetailScreenDesign(
        state = state,
        onEvent = onEvent,
        onBack = onBack,
        mediaId = mediaId

    )
}

@Composable
fun MediaDetailScreenDesign(
    mediaId: String,
    state: MediaDetailState,
    onEvent: (MediaDetailEvent) -> Unit,
    onBack: () -> Unit
) {


    LaunchedEffect(mediaId) {
        onEvent(MediaDetailEvent.LoadMedia(mediaId))
    }

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onBack()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.media != null -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(text = state.media.name, style = MaterialTheme.typography.titleLarge)

                    AsyncImage(
                        model = state.media.url,
                        contentDescription = "Media Preview",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Size: ${state.media.size}")
                    Text(text = "Uploaded on: ${state.media.uploadDate}")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { onEvent(MediaDetailEvent.DownloadMedia) }) {
                            Text("Download")
                        }

                        Button(
                            onClick = { onEvent(MediaDetailEvent.DeleteMedia) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Delete")
                        }
                    }
                }

            }

            else -> {
                Text(
                    text = "Media not found",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}






