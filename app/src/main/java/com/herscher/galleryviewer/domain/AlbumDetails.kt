package com.herscher.galleryviewer.domain

data class AlbumDetails(
    val id: Int,
    val title: String,
    val photos: List<Photo>,
)