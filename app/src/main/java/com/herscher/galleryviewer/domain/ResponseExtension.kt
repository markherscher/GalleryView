package com.herscher.galleryviewer.domain

import retrofit2.Response

/**
 * Converts the specified [Response] to a [Result], using the specified mapper if it was
 * successful. If it was not successful, a [Result.NetworkError] is created.
 *
 * @param successMapper called to map the response data to the result data
 * @return the appropriate result instance
 */
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