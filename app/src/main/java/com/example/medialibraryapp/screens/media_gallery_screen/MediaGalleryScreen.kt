package com.example.medialibraryapp.screens.media_gallery_screen

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.local.room.entities.MediaEntity
import com.example.medialibraryapp.R
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
fun MediaGalleryScreen(
    onMediaClick: (MediaEntity) -> Unit
) {

    val viewModel: MediaGalleryViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    MediaGalleryScreenDesign(
        state = state,
        onEvent = onEvent,
        onMediaClick = onMediaClick

    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MediaGalleryScreenDesignPreview() {
    MediaGalleryScreenDesign(
        state = MediaGalleryState(),
        onEvent = {},
        onMediaClick = {}
    )
}

@Composable
private fun MediaGalleryScreenDesign(
    state: MediaGalleryState,
    onEvent: (MediaGalleryEvent) -> Unit,
    onMediaClick: (MediaEntity) -> Unit
) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val contentResolver = context.contentResolver
            val type = contentResolver.getType(it) ?: "unknown"
            val fileName = getFileName(context, it) ?: UUID.randomUUID().toString()
            onEvent(MediaGalleryEvent.UploadMedia(it, fileName, type))
        }
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(), // Semi-transparent background
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
                items(state.mediaList.size) { index ->
                    val media = state.mediaList[index]
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onMediaClick(media) }
                    ) {
                        Column {
                            AsyncImage(
                                model = media.url,
                                contentDescription = null,
                                modifier = Modifier.size(120.dp)
                            )
                            Text(text = media.name, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { launcher.launch("*/*") }, // Allow both images and videos
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = CircleShape,
            containerColor = Color.Blue
        ) {
            Text(text = stringResource(R.string.upload), color = Color.White)
        }
    }
}

// Function to extract file name from URI
fun getFileName(context: Context, uri: Uri): String? {
    var fileName: String? = null
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1) {
                fileName = it.getString(index)
            }
        }
    }
    return fileName
}




