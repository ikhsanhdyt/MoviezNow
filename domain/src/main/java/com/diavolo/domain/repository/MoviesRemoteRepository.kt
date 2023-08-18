package com.diavolo.domain.repository

import com.diavolo.model.GenreResponse
import com.diavolo.model.MoviesResponse
import io.reactivex.Single

interface MoviesRemoteRepository {

    fun getGenreMovies(): Single<GenreResponse>

    fun getMoviesByGenre(genreId: String, page: Int): Single<MoviesResponse>

}