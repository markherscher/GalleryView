package com.herscher.galleryviewer

import com.herscher.galleryviewer.api.GalleryApi
import com.herscher.galleryviewer.domain.GalleryApiRepository
import com.herscher.galleryviewer.domain.GalleryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient): GalleryApi {
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build()

        return retrofit.create(GalleryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGalleryRepository(galleryApi: GalleryApi): GalleryRepository {
        return GalleryApiRepository(galleryApi)
    }
}