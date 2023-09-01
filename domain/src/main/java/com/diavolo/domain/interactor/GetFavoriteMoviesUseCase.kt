package com.diavolo.domain.interactor

import com.diavolo.domain.repository.MoviesLocalRepository
import com.diavolo.model.Movie
import io.reactivex.Observable

class GetFavoriteMoviesUseCase(private val moviesLocalRepository: MoviesLocalRepository) {

    fun execute(): Observable<List<Movie>> {
        return moviesLocalRepository.getFavoriteMovies()
    }

}