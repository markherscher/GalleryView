package com.herscher.galleryviewer.domain

import retrofit2.Response

fun <T, R> Response<T>.toResult(successMapper: (T) -> R): Result<R> {
    return if (this.isSuccessful) {
        val body = this.body()
        if (body == null) {
            Result.NetworkError(
                statusCode = null,
                message = "empty response body",
            )
        } else {
            Result.Success(successMapper(body))
        }
    } else {
        Result.NetworkError(
            statusCode = this.code(),
            message = this.message(),
        )
    }
}