package com.diavolo.domain.interactor

import com.diavolo.domain.repository.MoviesRemoteRepository
import com.diavolo.model.GenreResponse
import io.reactivex.Single

/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
class GetGenreMoviesUseCase(private val moviesRemoteRepository: MoviesRemoteRepository) {
    fun execute(): Single<GenreResponse> {
        return moviesRemoteRepository.getGenreMovies()
    }
}