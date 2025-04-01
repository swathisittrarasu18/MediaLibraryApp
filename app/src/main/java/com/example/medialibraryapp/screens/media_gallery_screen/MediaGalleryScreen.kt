package com.example.medialibraryapp.screens.media_gallery_screen

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.local.room.entities.MediaEntity
import com.example.medialibraryapp.R
import com.example.medialibraryapp.screens.media_detail_screen.getFileSizeInKB
import com.example.medialibraryapp.ui.theme.extensions.appBarColor
import com.example.medialibraryapp.ui.theme.extensions.appBarTitleColor
import com.example.medialibraryapp.ui.theme.extensions.imageDescriptionTextColor
import com.example.medialibraryapp.utils.composes.DeviceUtils.isTablet
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

    val columns = if (isTablet()) 4 else 2
    val imageSize = if (isTablet()) 180.dp else 120.dp
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(PickVisualMedia()) { uri: Uri? ->
        uri?.let {
            val contentResolver = context.contentResolver
            val type = contentResolver.getType(it) ?: "unknown"
            val fileName = getFileName(context, it) ?: UUID.randomUUID().toString()
            val fileSize = getFileSizeInKB(context, it) ?: 0L
            onEvent(MediaGalleryEvent.UploadMedia(it, fileName, type, fileSize))
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

        Column {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(appBarColor),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(R.string.media_gallery),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = appBarTitleColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 12.dp)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.mediaList.size) { index ->
                    val media = state.mediaList[index]
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onMediaClick(media) }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AsyncImage(
                                model = media.url,
                                contentDescription = null,
                                modifier = Modifier.size(imageSize)
                            )

                            Text(
                                text = media.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = imageDescriptionTextColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

            }
        }

        FloatingActionButton(
            onClick = { launcher.launch(PickVisualMediaRequest(PickVisualMedia.ImageAndVideo)) }, // Allow both images and videos
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = CircleShape,
            containerColor = appBarColor
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
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





