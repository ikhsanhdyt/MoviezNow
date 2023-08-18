package com.diavolo.movieznow.ui.home.viewModel

import androidx.lifecycle.ViewModel
import com.diavolo.domain.interactor.GetMoviesByGenreUseCase
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
class MovieListViewModel(private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase) :
    ViewModel() {

    private val stateFlow = MutableStateFlow<Resource<List<Movie>>>(Resource.empty())
    private var currentPage = 1
    private var lastPage = 1

    var disposable: Disposable? = null

    val movieListState: StateFlow<Resource<List<Movie>>>
        get() = stateFlow

    fun fetchPopularMovies() {
        stateFlow.value = Resource.loading()

        disposable = getMoviesByGenreUseCase.execute("28", currentPage)
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

    fun fetchNextMovies() {
        currentPage++
        fetchPopularMovies()
    }

    fun refreshMovies() {
        currentPage = 1
        fetchPopularMovies()
    }

    fun isFirstPage(): Boolean {
        return currentPage == 1
    }

    fun isLastPage(): Boolean {
        return currentPage == lastPage
    }
}