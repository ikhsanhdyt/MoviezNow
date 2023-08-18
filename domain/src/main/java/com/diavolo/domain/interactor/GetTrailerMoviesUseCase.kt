package com.diavolo.domain.interactor

import com.diavolo.domain.repository.MoviesRemoteRepository
import com.diavolo.model.MoviesResponse
import com.diavolo.model.TrailerResponse
import io.reactivex.Single

class GetTrailerMoviesUseCase(private val moviesRemoteRepository: MoviesRemoteRepository) {

    fun execute(movieId: String): Single<TrailerResponse> {
        return moviesRemoteRepository.getTrailerMovies(movieId)
    }

}