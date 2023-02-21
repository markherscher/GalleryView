package com.herscher.galleryviewer.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GalleryApi {
    @GET("/albums")
    suspend fun getAlbumList(): Response<List<AlbumResponse>>

    @GET("/photos")
    suspend fun getPhotos(@Query("albumId") albumId: Int): Response<List<PhotoResponse>>
}