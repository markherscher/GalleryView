package com.herscher.galleryviewer.domain

import java.lang.Exception

/**
 * Represents a generic result of an operation, with one success state and several specific error
 * states. Additional error states can be added as necessary and appropriate (for example,
 * `UnknownHostException` could be mapped to a new `NoInternetError`).
 *
 * T represents the type of the success data.
 */
sealed class Result<out T> {
    /**
     * The success result.
     * @param data the data for the success
     */
    data class Success<T>(val data: T) : Result<T>()

    /**
     * The error result for an exception or general catch-all error.
     *
     * @param exception the optional exception that was thrown
     * @param message the optional message describing the error
     */
    data class GeneralError(
        val exception: Exception? = null,
        val message: String? = exception?.message,
    ) : Result<Nothing>()

    /**
     * The error result for a failed network request.
     *
     * @param statusCode the HTTP status code of the response
     * @param message the message of the response
     */
    data class NetworkError(
        val statusCode: Int,
        val message: String,
    ) : Result<Nothing>()
}