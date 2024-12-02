package com.example.hw_02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

data class GiphyResponse(val data: List<GifData>)
data class GifData(val images: GifImages)
data class GifImages(val original: ImageUrl)
data class ImageUrl(val url: String)

class MainActivity : ComponentActivity() {

    private val viewModel: GiphyViewModel by viewModels {
        GiphyViewModelFactory(RepositoryProvider.provideGiphyRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiphyAppTheme {
                GiphyApp(viewModel = viewModel)
            }
        }
    }
}
