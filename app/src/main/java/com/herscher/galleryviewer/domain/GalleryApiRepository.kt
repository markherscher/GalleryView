package com.herscher.galleryviewer.domain

import com.herscher.galleryviewer.api.GalleryApi

class GalleryApiRepository(
    private val galleryApi: GalleryApi
) : GalleryRepository {
    override suspend fun getAllAlbums(): Result<List<Album>> {
        return galleryApi.getAlbumList().toResult { albumList ->
            albumList.map { albumResponse ->
                Album(
                    id = albumResponse.id,
                    title = albumResponse.title,
                )
            }
        }
    }

    override suspend fun getPhotosForAlbum(albumId: Int): Result<List<Photo>> {
        return galleryApi.getPhotos(albumId).toResult { photoResponses ->
            photoResponses.map { photoResponse ->
                Photo(
                    title = photoResponse.title,
                    url = photoResponse.url,
                    thumbnailUrl = photoResponse.thumbnailUrl,
                )
            }
        }
    }
}
