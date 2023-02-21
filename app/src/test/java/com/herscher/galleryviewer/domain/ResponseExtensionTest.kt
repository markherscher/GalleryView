package com.herscher.galleryviewer.domain

import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class ResponseExtensionTest {
    @Test
    fun `successful response map test`() {
        val response = Response.success("the response body")
        assertEquals(
            Result.Success(15),
            response.toResult { 15 }
        )
    }

    @Test
    fun `empty body response map test`() {
        val response = Response.success(null)
        assertEquals(
            Result.NetworkError(statusCode = null, message = "empty response body"),
            response.toResult { "should not matter" }
        )
    }

    @Test
    fun `failed response map test`() {
        val response = Response.error<Int>(404, "hello world".toResponseBody())
        assertEquals(
            Result.NetworkError(statusCode = 404, message = "Response.error()"),
            response.toResult { "should not matter" }
        )
    }
}