package com.example.hw_02

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

sealed interface UiState {
    data object Loading : UiState
    data object Error : UiState
    data class GifListState(val gifStates: List<GifState>) : UiState
}

sealed interface GifState {
    data class Gif(val data: GifData) : GifState
    data class GifError(val data: GifData) : GifState
}

class GiphyViewModelFactory(private val repository: GiphyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GiphyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GiphyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class GiphyViewModel(private val repository: GiphyRepository) : ViewModel() {
    var uiState by mutableStateOf<UiState>(UiState.Loading)
        private set

    private var gifs: List<GifData> = emptyList()
    private var offset = 0
    private val limit = 50
    private var isLoading = false

    init {
        loadGifs()
    }

    fun loadGifs() {
        if (isLoading) return
        fetchGifs()
    }

    fun reloadGifs() {
        if (isLoading) return
        fetchGifs()
    }

    private fun fetchGifs() {
        isLoading = true
        uiState = UiState.Loading

        viewModelScope.launch {
            try {
                val newGifs = repository.getTrendingGifs(offset, limit)
                gifs = newGifs
                val gifStates = newGifs.map { GifState.Gif(it) }
                uiState = UiState.GifListState(gifStates)
                offset += limit
            } catch (e: Exception) {
                uiState = UiState.Error
            } finally {
                isLoading = false
            }
        }
    }

    fun reloadGif(gif: GifData) {
        val gifErrorState = GifState.GifError(gif)
        val currentState = (uiState as? UiState.GifListState)?.gifStates?.toMutableList()

        val index = currentState?.indexOfFirst { it is GifState.Gif && (it.data == gif) }

        if (index != null && index >= 0) {
            currentState[index] = gifErrorState
            uiState = UiState.GifListState(currentState)

            viewModelScope.launch {
                try {
                    val newGif = repository.getTrendingGifs(index, 1).first()
                    val updatedGifState = GifState.Gif(newGif)

                    currentState[index] = updatedGifState
                    uiState = UiState.GifListState(currentState)
                } catch (e: Exception) {
                    currentState[index] = GifState.GifError(gif)
                    uiState = UiState.GifListState(currentState)
                }
            }
        }
    }
}
