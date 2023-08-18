package com.diavolo.movieznow.ui.home.viewModel

import androidx.lifecycle.ViewModel
import com.diavolo.domain.interactor.GetGenreMoviesUseCase
import com.diavolo.domain.interactor.GetMoviesByGenreUseCase
import com.diavolo.model.Genre
import com.diavolo.model.Movie
import com.diavolo.movieznow.data.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
class MovieListViewModel(
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase,
    private val getGenreMoviesUseCase: GetGenreMoviesUseCase
) :
    ViewModel() {

    private val stateFlow = MutableStateFlow<Resource<List<Movie>>>(Resource.empty())
    private val genreStateFlow = MutableStateFlow<Resource<List<Genre>>>(Resource.empty())
    private var currentPage = 1
    private var lastPage = 1

    private val genreIdStateFlow = MutableStateFlow<Resource<String>>(Resource.empty())

    var disposable: Disposable? = null

    val movieListState: StateFlow<Resource<List<Movie>>>
        get() = stateFlow
    val genreListState: StateFlow<Resource<List<Genre>>>
        get() = genreStateFlow

    val genreIdState: StateFlow<Resource<String>>
        get() = genreIdStateFlow

    private val _genreId = MutableStateFlow<String>("")
    val genreId: StateFlow<String> get() = _genreId

    private fun fetchMovieListMovies() {
        stateFlow.value = Resource.loading()

        disposable = getMoviesByGenreUseCase.execute(_genreId.value, currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                lastPage = res.total_pages
                stateFlow.value = Resource.success(res.results)
            }, { throwable ->
                lastPage = currentPage // prevent loading more pages
                throwable.localizedMessage?.let {
                    stateFlow.value = Resource.error(it)
                }
            })
    }

     fun fetchGenreListMovies() {
        genreStateFlow.value = Resource.loading()

        disposable = getGenreMoviesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                genreStateFlow.value = Resource.success(res.genres)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    genreStateFlow.value = Resource.error(it)
                }
            })
    }

    fun fetchNextMovies() {
        currentPage++
        fetchMovieListMovies()
    }

    fun refreshMovies() {
        currentPage = 1
        fetchMovieListMovies()
    }

    fun isFirstPage(): Boolean {
        return currentPage == 1
    }

    fun isLastPage(): Boolean {
        return currentPage == lastPage
    }

    fun updateGenreId(newGenreId: String) {
        _genreId.value = newGenreId
    }
}