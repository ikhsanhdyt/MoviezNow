package com.diavolo.data.sources.remote.api

import com.diavolo.data.sources.remote.auth.MoviesAuthInterceptor
import com.diavolo.data.sources.remote.service.MovieService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
    const val YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com/watch?v="
    const val YOUTUBE_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/"


    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(OkHttpClient.Builder()
            .addInterceptor(MoviesAuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build())
        .build()

    fun movieService(): MovieService = retrofit.create(
        MovieService::class.java)

}