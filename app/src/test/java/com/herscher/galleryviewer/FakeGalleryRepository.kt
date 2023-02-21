package com.herscher.galleryviewer

import com.herscher.galleryviewer.domain.Album
import com.herscher.galleryviewer.domain.GalleryRepository
import com.herscher.galleryviewer.domain.Photo
import com.herscher.galleryviewer.domain.Result

class FakeGalleryRepository : GalleryRepository {
    var albumResponse: Result<List<Album>>? = null
    var photosResponse: Result<List<Photo>>? = null
    var receivedAlbumId: Int? = null

    override suspend fun getAllAlbums(): Result<List<Album>> {
        return albumResponse ?: error("unexpected function call")
    }

    override suspend fun getPhotosForAlbum(albumId: Int): Result<List<Photo>> {
        receivedAlbumId = albumId
        return photosResponse ?: error("unexpected function call")
    }
}