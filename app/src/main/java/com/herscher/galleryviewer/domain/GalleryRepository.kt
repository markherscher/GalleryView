package com.herscher.galleryviewer.domain

/**
 * Provides access to albums and photos of galleries.
 */
interface GalleryRepository {
    /**
     * Gets all albums.
     *
     * @return the result of the operation
     */
    suspend fun getAllAlbums(): Result<List<Album>>

    /**
     * Gets all photos for the album with the specified ID.
     *
     * @param albumId the ID of the album
     * @return the result of the operation
     */
    suspend fun getPhotosForAlbum(albumId: Int): Result<List<Photo>>
}