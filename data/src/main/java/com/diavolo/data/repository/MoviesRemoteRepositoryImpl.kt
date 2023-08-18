package com.diavolo.data.repository

import com.diavolo.data.sources.remote.api.ApiClient
import com.diavolo.data.sources.remote.mapper.MoviesRemoteMapper
import com.diavolo.domain.repository.MoviesRemoteRepository
import com.diavolo.model.GenreResponse
import com.diavolo.model.MoviesResponse
import io.reactivex.Single


class MoviesRemoteRepositoryImpl(private val moviesRemoteMapper: MoviesRemoteMapper) :
    MoviesRemoteRepository {
    override fun getGenreMovies(): Single<GenreResponse> {
        return ApiClient.movieService().getGenreMovies().map {
            moviesRemoteMapper.mapGenreFromRemote(it)
        }
    }

    override fun getMoviesByGenre(genreId: String, page: Int): Single<MoviesResponse> {
        return ApiClient.movieService().getMovieListByGenre(genreId, page).map {
            moviesRemoteMapper.mapFromRemote(it)
        }
    }


}