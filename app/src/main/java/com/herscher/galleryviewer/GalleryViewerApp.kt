package com.herscher.galleryviewer

import android.app.Application
import com.herscher.galleryviewer.api.GalleryApi
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@HiltAndroidApp
class GalleryViewerApp : Application() {
    override fun onCreate() {
        super.onCreate()

//        val retrofit = Retrofit.Builder()
//            .client(OkHttpClient())
//            .addConverterFactory(MoshiConverterFactory.create())
//            .baseUrl("https://jsonplaceholder.typicode.com/")
//            .build()
//
//        api = retrofit.create(GalleryApi::class.java)
    }

    companion object {
        //lateinit var api: GalleryApi
    }
}