package com.herscher.galleryviewer.domain

interface GalleryRepository {
    suspend fun getAllAlbums(): Result<List<Album>>

    suspend fun getPhotosForAlbum(albumId: Int): Result<List<Photo>>
}