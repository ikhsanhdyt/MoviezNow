package com.diavolo.domain.interactor

import com.diavolo.domain.repository.MoviesRemoteRepository
import com.diavolo.model.MoviesResponse
import com.diavolo.model.ReviewMoviesResponse
import io.reactivex.Single

class GetReviewMoviesUseCase(private val moviesRemoteRepository: MoviesRemoteRepository) {

    fun execute(movieId: String, page: Int): Single<ReviewMoviesResponse> {
        return moviesRemoteRepository.getReviewMovies(movieId,page)
    }

}