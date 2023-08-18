package com.diavolo.domain.repository

import com.diavolo.model.GenreResponse
import com.diavolo.model.MoviesResponse
import com.diavolo.model.ReviewMoviesResponse
import com.diavolo.model.Trailer
import com.diavolo.model.TrailerResponse
import io.reactivex.Single

interface MoviesRemoteRepository {

    fun getGenreMovies(): Single<GenreResponse>
    fun getMoviesByGenre(genreId: String, page: Int): Single<MoviesResponse>
    fun getTrailerMovies(movieId: String): Single<TrailerResponse>
    fun getReviewMovies(movieId: String, page: Int): Single<ReviewMoviesResponse>

}