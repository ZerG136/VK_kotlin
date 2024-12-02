package com.example.hw_02

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest

@Composable
fun GifList(gifs: List<GifState>, onLoadMore: () -> Unit, onRetryGif: (GifData) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(10.dp)
    ) {
        items(gifs) {
            gifState ->
            when (gifState) {
                is GifState.GifError -> GifError(gif = gifState.data, onRetry = { onRetryGif(gifState.data) })
                is GifState.Gif -> GifImage(gifState.data)
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
                onLoadMore()
            }
        }
    }
}

@Composable
fun GifImage(gif: GifData) {
    val context = LocalContext.current

    val imageUrl = gif.images.original.url

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .decoderFactory(GifDecoder.Factory())
            .build(),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(10.dp),
        placeholder = painterResource(R.drawable.placeholder),
        error = painterResource(R.drawable.error_placeholder)
    )
}

@Composable
fun GifError(gif: GifData, onRetry: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Ошибка загрузки")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Повторить")
            }
        }
    }
}
