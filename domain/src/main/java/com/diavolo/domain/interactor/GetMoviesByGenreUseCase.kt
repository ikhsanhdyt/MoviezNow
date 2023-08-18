package com.diavolo.domain.interactor

import com.diavolo.domain.repository.MoviesRemoteRepository
import com.diavolo.model.MoviesResponse
import io.reactivex.Single

class GetMoviesByGenreUseCase(private val moviesRemoteRepository: MoviesRemoteRepository) {

    fun execute(genreId: String, page: Int): Single<MoviesResponse> {
        return moviesRemoteRepository.getMoviesByGenre(genreId, page)
    }

}