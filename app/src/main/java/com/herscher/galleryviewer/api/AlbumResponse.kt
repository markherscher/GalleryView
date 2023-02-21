package com.herscher.galleryviewer.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumResponse(
    val id: Int,
    val title: String,
    val userId: Int,
)