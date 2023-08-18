package com.diavolo.data.sources.remote.service

import com.diavolo.data.sources.remote.model.RemoteGenreMoviesResponse
import com.diavolo.data.sources.remote.model.RemoteMoviesResponse
import com.diavolo.data.sources.remote.model.RemoteReviewListResponse
import com.diavolo.data.sources.remote.model.RemoteTrailerResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("genre/movie/list")
    fun getGenreMovies(): Single<RemoteGenreMoviesResponse>

    @GET("discover/movie")
    fun getMovieListByGenre(
        @Query("with_genres") genreId: String,
        @Query("page") page: Int
    ): Single<RemoteMoviesResponse>

    @GET("movie/{movie_id}/videos")
     fun getTrailerList(@Path("movie_id") movieId: String): Single<RemoteTrailerResponse>

    @GET("movie/{movie_id}/reviews")
     fun getReviewList(@Path("movie_id") movieId: String,@Query("page") page: Int): Single<RemoteReviewListResponse>

}