package com.diavolo.data.sources.remote.service

import com.diavolo.data.sources.remote.model.RemoteGenreMoviesResponse
import com.diavolo.data.sources.remote.model.RemoteMoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("genre/movie/list")
    fun getGenreMovies(): Single<RemoteGenreMoviesResponse>

    @GET("discover/movie")
    fun getMovieListByGenre(
        @Query("with_genres") genreId: String,
        @Query("page") page: Int
    ): Single<RemoteMoviesResponse>

}