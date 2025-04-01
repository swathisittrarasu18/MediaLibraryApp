package com.example.medialibraryapp.screens.media_detail_screen

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.medialibraryapp.R
import com.example.medialibraryapp.ui.theme.extensions.imageDescriptionTextColor
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
                    Text(
                        modifier = Modifier.padding(vertical = 10.dp),
                        text = state.media.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = imageDescriptionTextColor
                    )

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

                    Text(text = "Size: ${state.media.size} KB")
                    Text(text = "Uploaded on: ${formatTimestamp(state.media.uploadDate)}")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { onEvent(MediaDetailEvent.DownloadMedia) }) {
                            Text(stringResource(R.string.download))
                        }

                        Button(
                            onClick = { onEvent(MediaDetailEvent.DismissConfirmationDialog(true)) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text(stringResource(R.string.delete))
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

    if (state.isShowDeleteConfirmationDialog) {
        DeleteConfirmationDialog(
            onClick = { onEvent(MediaDetailEvent.DeleteMedia) },
            onDismiss = { onEvent(MediaDetailEvent.DismissConfirmationDialog(false)) }
        )
    }
}

fun getFileSizeInKB(context: Context, uri: Uri): Long? {
    return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        if (sizeIndex != -1 && cursor.moveToFirst()) {
            convertFileSizeToKB(cursor.getLong(sizeIndex))
            // Convert bytes to KB
        } else {
            null
        }
    }
}


private fun convertFileSizeToKB(sizeInBytes: Long): Long {
    return when {
        sizeInBytes < 1024 -> 1L  // Minimum 1KB for files <1KB
        sizeInBytes < 1024 * 1024 -> sizeInBytes / 1024  // KB range
        sizeInBytes < 1024 * 1024 * 1024 -> (sizeInBytes / 1024)  // MB→KB
        else -> (sizeInBytes / 1024)  // GB→KB
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US)
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(Date(timestamp))
}




