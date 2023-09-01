package com.diavolo.domain.interactor

import com.diavolo.domain.repository.MoviesLocalRepository
import com.diavolo.model.Movie
import io.reactivex.Single

class GetFavoriteMovieUseCase(private val moviesLocalRepository: MoviesLocalRepository) {

    fun execute(id: Int): Single<Movie> {
        return moviesLocalRepository.getFavoriteMovie(id)
    }

}