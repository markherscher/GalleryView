package com.herscher.galleryviewer.viewmodel

import com.herscher.MainDispatcherTestRule
import com.herscher.galleryviewer.FakeGalleryRepository
import com.herscher.galleryviewer.domain.Photo
import com.herscher.galleryviewer.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PhotosViewModelTest {
    @get:Rule
    val mainDispatcherTestRule = MainDispatcherTestRule()

    private lateinit var fakeGalleryRepository: FakeGalleryRepository
    private lateinit var sut: PhotosViewModel

    @Before
    fun setup() {
        fakeGalleryRepository = FakeGalleryRepository()
        sut = PhotosViewModel(fakeGalleryRepository)
    }

    @Test
    fun `starts in loading state test`() {
        Assert.assertEquals(PhotosViewModel.UiState.Loading, sut.uiState.value)
    }

    @Test
    fun `view created loads data test`() = runTest {
        fakeGalleryRepository.photosResponse = Result.Success(
            listOf(
                Photo(title = "t1", url = "url1", thumbnailUrl = "t1"),
                Photo(title = "t2", url = "url2", thumbnailUrl = "t2")
            )
        )

        sut.viewCreated(9)
        advanceUntilIdle()

        Assert.assertEquals(9, fakeGalleryRepository.receivedAlbumId)
        Assert.assertEquals(
            PhotosViewModel.UiState.Content(
                listOf(
                    Photo(title = "t1", url = "url1", thumbnailUrl = "t1"),
                    Photo(title = "t2", url = "url2", thumbnailUrl = "t2")
                )
            ),
            sut.uiState.value
        )
    }

    @Test
    fun `refresh loads data test`() = runTest {
        fakeGalleryRepository.photosResponse = Result.Success(
            listOf(Photo(title = "t1", url = "url1", thumbnailUrl = "t1"))
        )

        sut.viewCreated(9)
        advanceUntilIdle()

        Assert.assertEquals(9, fakeGalleryRepository.receivedAlbumId)
        Assert.assertEquals(
            PhotosViewModel.UiState.Content(
                listOf(Photo(title = "t1", url = "url1", thumbnailUrl = "t1"))
            ),
            sut.uiState.value
        )
    }

    @Test
    fun `error is reported test`() = runTest {
        fakeGalleryRepository.photosResponse = Result.NetworkError(
            statusCode = 500, message = "hello world"
        )

        sut.refresh()
        advanceUntilIdle()

        Assert.assertEquals(PhotosViewModel.UiState.Error, sut.uiState.value)
    }
}