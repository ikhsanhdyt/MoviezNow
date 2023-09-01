package com.diavolo.movieznow.ui.favorite.viewModel

import androidx.lifecycle.ViewModel
import com.diavolo.domain.interactor.GetFavoriteMoviesUseCase
import com.diavolo.model.Movie
import com.diavolo.movieznow.data.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Written with passion by Ikhsan Hidayat on 01/09/2023.
 */
class FavoriteViewModel(private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase) : ViewModel() {
    private val stateFlow = MutableStateFlow<Resource<List<Movie>>>(Resource.empty())
    var disposable: Disposable? = null

    val favoriteMoviesState: StateFlow<Resource<List<Movie>>>
        get() = stateFlow

    fun fetchFavoriteMovies() {
        stateFlow.value = Resource.loading()

        disposable = getFavoriteMoviesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                stateFlow.value = Resource.success(res)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    stateFlow.value = Resource.error(it)
                }
            })
    }
}