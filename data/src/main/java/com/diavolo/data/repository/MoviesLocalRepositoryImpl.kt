package com.diavolo.data.repository

import android.content.Context
import com.diavolo.data.sources.local.db.MoviesDatabase
import com.diavolo.data.sources.local.mapper.MoviesLocalMapper
import com.diavolo.domain.repository.MoviesLocalRepository
import com.diavolo.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class MoviesLocalRepositoryImpl(
    private val context: Context,
    private val moviesLocalMapper: MoviesLocalMapper
) :
    MoviesLocalRepository {

    override fun getFavoriteMovies(): Observable<List<Movie>> {
        return MoviesDatabase.invoke(context).movieDao().getFavoriteMovies().map {
            it.map { movie ->
                moviesLocalMapper.mapFromLocal(movie)
            }
        }
    }

    override fun getFavoriteMovie(id: Int): Single<Movie> {
        return MoviesDatabase.invoke(context).movieDao().getFavoriteMovie(id).map {
            moviesLocalMapper.mapFromLocal(it)
        }
    }

    override fun addFavoriteMovie(movie: Movie): Completable {
        return MoviesDatabase.invoke(context).movieDao()
            .addFavoriteMovie(moviesLocalMapper.mapToLocal(movie))
    }

    override fun deleteFavoriteMovie(movie: Movie): Completable {
        return MoviesDatabase.invoke(context).movieDao()
            .deleteFavoriteMovie(moviesLocalMapper.mapToLocal(movie))
    }

    override fun updateFavoriteMovie(movie: Movie): Completable {
        return MoviesDatabase.invoke(context).movieDao()
            .updateFavoriteMovie(moviesLocalMapper.mapToLocal(movie))
    }

}