package com.herscher.galleryviewer.viewmodel

import com.herscher.MainDispatcherTestRule
import com.herscher.galleryviewer.FakeGalleryRepository
import com.herscher.galleryviewer.domain.Album
import com.herscher.galleryviewer.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AlbumsViewModelTest {
    @get:Rule
    val mainDispatcherTestRule = MainDispatcherTestRule()

    private lateinit var fakeGalleryRepository: FakeGalleryRepository
    private lateinit var sut: AlbumsViewModel

    @Before
    fun setup() {
        fakeGalleryRepository = FakeGalleryRepository()
        sut = AlbumsViewModel(fakeGalleryRepository)
    }

    @Test
    fun `starts in loading state test`() {
        assertEquals(AlbumsViewModel.UiState.Loading, sut.uiState.value)
    }

    @Test
    fun `view created loads data test`() = runTest {
        fakeGalleryRepository.albumResponse = Result.Success(
            listOf(Album(id = 1, title = "t1"), Album(id = 2, title = "t2"))
        )

        sut.viewCreated()
        advanceUntilIdle()

        assertEquals(
            AlbumsViewModel.UiState.Content(
                listOf(Album(id = 1, title = "t1"), Album(id = 2, title = "t2"))
            ),
            sut.uiState.value
        )
    }

    @Test
    fun `refresh loads data test`() = runTest {
        fakeGalleryRepository.albumResponse = Result.Success(
            listOf(Album(id = 1, title = "t1"), Album(id = 2, title = "t2"))
        )

        sut.refresh()
        advanceUntilIdle()

        assertEquals(
            AlbumsViewModel.UiState.Content(
                listOf(Album(id = 1, title = "t1"), Album(id = 2, title = "t2"))
            ),
            sut.uiState.value
        )
    }

    @Test
    fun `error is reported test`() = runTest {
        fakeGalleryRepository.albumResponse = Result.NetworkError(
            statusCode = 500, message = "hello world"
        )

        sut.refresh()
        advanceUntilIdle()

        assertEquals(AlbumsViewModel.UiState.Error, sut.uiState.value)
    }
}