package com.herscher.galleryviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herscher.galleryviewer.domain.Album
import com.herscher.galleryviewer.domain.GalleryRepository
import com.herscher.galleryviewer.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun viewCreated() {
        refresh()
    }

    fun refresh() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            val result = galleryRepository.getAllAlbums()
            _uiState.value = when (result) {
                is Result.GeneralError, is Result.NetworkError -> UiState.Error
                is Result.Success -> UiState.Content(result.data)
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Content(val albums: List<Album>) : UiState()
        object Error : UiState()
    }
}
