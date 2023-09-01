package com.diavolo.domain.interactor

import com.diavolo.domain.repository.MoviesLocalRepository
import com.diavolo.model.Movie
import io.reactivex.Completable

class AddFavoriteMovieUseCase(private val moviesLocalRepository: MoviesLocalRepository) {

    fun execute(movie: Movie): Completable {
        return moviesLocalRepository.addFavoriteMovie(movie)
    }

}