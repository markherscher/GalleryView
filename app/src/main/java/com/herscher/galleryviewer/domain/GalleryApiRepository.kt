package com.herscher.galleryviewer.domain

import com.herscher.galleryviewer.api.GalleryApi
import retrofit2.Response

class GalleryApiRepository(
    private val galleryApi: GalleryApi
) : GalleryRepository {
    override suspend fun getAllAlbums(): Result<List<Album>> {
        // Normally bad to wrap all exceptions, but we don't want the network call to crash the
        // app here. Things like Json encoding errors could be logged for visibility and fixing,
        // but that's outside the scope of this.
        val response = try {
            galleryApi.getAlbumList()
        } catch (e: Exception) {
            return Result.GeneralError(exception = e)
        }

        return response.toResult { albumList ->
            albumList.map { albumResponse ->
                Album(
                    id = albumResponse.id,
                    title = albumResponse.title,
                )
            }
        }
    }

    override suspend fun getPhotosForAlbum(albumId: Int): Result<List<Photo>> {
        // Normally bad to wrap all exceptions, but we don't want the network call to crash the
        // app here. Things like Json encoding errors could be logged for visibility and fixing,
        // but that's outside the scope of this.
        val response = try {
            galleryApi.getPhotos(albumId)
        } catch (e: Exception) {
            return Result.GeneralError(exception = e)
        }

        return response.toResult { photoResponses ->
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
