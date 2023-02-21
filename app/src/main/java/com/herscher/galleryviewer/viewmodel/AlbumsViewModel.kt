package com.herscher.galleryviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herscher.galleryviewer.api.AlbumResponse
import com.herscher.galleryviewer.api.GalleryApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val galleryApi: GalleryApi
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            // tODO move/fix
            val response = galleryApi.getAlbumList()
            val body = response.body()

            _uiState.value = if (body == null) {
                UiState.Error
            } else {
                UiState.Content(body)
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Content(val albums: List<AlbumResponse>) : UiState()
        object Error : UiState()
    }
}