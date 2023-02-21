package com.herscher.galleryviewer.domain

import java.lang.Exception

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()

    data class GeneralError(
        val exception: Exception? = null,
        val message: String? = null,
    ) : Result<Nothing>()

    data class NetworkError(
        val statusCode: Int?,
        val message: String,
    ) : Result<Nothing>()
}