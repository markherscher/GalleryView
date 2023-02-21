package com.herscher.galleryviewer

import com.herscher.galleryviewer.api.GalleryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideApi(): GalleryApi {
        val retrofit = Retrofit.Builder()
            .client(OkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build()

        return retrofit.create(GalleryApi::class.java)
    }
}