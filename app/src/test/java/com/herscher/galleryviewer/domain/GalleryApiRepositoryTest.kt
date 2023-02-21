package com.herscher.galleryviewer.domain

import com.herscher.galleryviewer.api.AlbumResponse
import com.herscher.galleryviewer.api.GalleryApi
import com.herscher.galleryviewer.api.PhotoResponse
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class GalleryApiRepositoryTest {
    private lateinit var fakeGalleryApi: FakeGalleryApi
    private lateinit var sut: GalleryApiRepository

    @Before
    fun setup() {
        fakeGalleryApi = FakeGalleryApi()
        sut = GalleryApiRepository(fakeGalleryApi)
    }

    @Test
    fun `get all albums success test`() = runBlocking {
        fakeGalleryApi.getAlbumListResponse = {
            Response.success(
                listOf(
                    AlbumResponse(id = 1, title = "title 1", userId = 10),
                    AlbumResponse(id = 2, title = "title 2", userId = 9),
                    AlbumResponse(id = 3, title = "title 3", userId = 8),
                )
            )
        }

        assertEquals(
            Result.Success(
                listOf(
                    Album(id = 1, title = "title 1"),
                    Album(id = 2, title = "title 2"),
                    Album(id = 3, title = "title 3"),
                )
            ),
            sut.getAllAlbums()
        )
    }

    @Test
    fun `get all albums exception thrown test`() = runBlocking {
        // Must use same instance for the assertEquals to work
        val exception = IllegalAccessException("hi")
        fakeGalleryApi.getAlbumListResponse = { throw exception }

        assertEquals(
            Result.GeneralError(exception = exception, message = "hi"),
            sut.getAllAlbums()
        )
    }

    @Test
    fun `get all albums failed network test`() = runBlocking {
        fakeGalleryApi.getAlbumListResponse =
            { Response.error(404, "hello world".toResponseBody()) }

        assertEquals(
            Result.NetworkError(statusCode = 404, "Response.error()"),
            sut.getAllAlbums()
        )
    }

    @Test
    fun `get photos success test`() = runBlocking {
        fakeGalleryApi.getPhotosResponse = {
            Response.success(
                listOf(
                    PhotoResponse(
                        albumId = 99,
                        id = 1,
                        title = "title 1",
                        url = "url1",
                        thumbnailUrl = "thumbnail1"
                    ),
                    PhotoResponse(
                        albumId = 98,
                        id = 2,
                        title = "title 2",
                        url = "url2",
                        thumbnailUrl = "thumbnail2"
                    ),
                )
            )
        }

        val result = sut.getPhotosForAlbum(4)
        assertEquals(4, fakeGalleryApi.receivedAlbumId)
        assertEquals(
            Result.Success(
                listOf(
                    Photo(title = "title 1", url = "url1", thumbnailUrl = "thumbnail1"),
                    Photo(title = "title 2", url = "url2", thumbnailUrl = "thumbnail2"),
                )
            ),
            result
        )
    }

    @Test
    fun `get photos exception thrown test`() = runBlocking {
        // Must use same instance for the assertEquals to work
        val exception = IllegalAccessException("hi")
        fakeGalleryApi.getPhotosResponse = { throw exception }

        assertEquals(
            Result.GeneralError(exception = exception, message = "hi"),
            sut.getPhotosForAlbum(1)
        )
    }

    @Test
    fun `get photos failed network test`() = runBlocking {
        fakeGalleryApi.getPhotosResponse =
            { Response.error(404, "hello world".toResponseBody()) }

        assertEquals(
            Result.NetworkError(statusCode = 404, "Response.error()"),
            sut.getPhotosForAlbum(1)
        )
    }

    private class FakeGalleryApi : GalleryApi {
        var getAlbumListResponse: (() -> Response<List<AlbumResponse>>)? = null
        var getPhotosResponse: (() -> Response<List<PhotoResponse>>)? = null
        var receivedAlbumId: Int? = null

        override suspend fun getAlbumList(): Response<List<AlbumResponse>> {
            return getAlbumListResponse?.invoke() ?: error("unexpected call to gallery api")
        }

        override suspend fun getPhotos(albumId: Int): Response<List<PhotoResponse>> {
            receivedAlbumId = albumId
            return getPhotosResponse?.invoke() ?: error("unexpected call to gallery api")
        }
    }
}