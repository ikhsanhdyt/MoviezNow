package com.diavolo.movieznow.ui.details.viewModel

import androidx.lifecycle.ViewModel
import com.diavolo.domain.interactor.GetReviewMoviesUseCase
import com.diavolo.domain.interactor.GetTrailerMoviesUseCase
import com.diavolo.model.ReviewMoviesResponse
import com.diavolo.model.TrailerResponse
import com.diavolo.movieznow.data.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
class MovieDetailsViewModel(
    private val getReviewMoviesUseCase: GetReviewMoviesUseCase,
    private val getTrailerMoviesUseCase: GetTrailerMoviesUseCase,
) : ViewModel() {
    private var currentPage = 1
    private var lastPage = 1

    private val trailerStateFlow = MutableStateFlow<Resource<TrailerResponse>>(Resource.empty())
    private val reviewStateFlow = MutableStateFlow<Resource<ReviewMoviesResponse>>(Resource.empty())
    var disposable: Disposable? = null
    val trailerMovieState: StateFlow<Resource<TrailerResponse>>
        get() = trailerStateFlow

    val reviewState: StateFlow<Resource<ReviewMoviesResponse>>
        get() = reviewStateFlow

    private val _movieId = MutableStateFlow<String>("")
    val movieId: StateFlow<String> get() = _movieId

    fun fetchTrailer() {
        trailerStateFlow.value = Resource.loading()
        disposable = getTrailerMoviesUseCase.execute(_movieId.value)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                trailerStateFlow.value = Resource.success(res)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    trailerStateFlow.value = Resource.error(it)
                }
            })

    }

    fun fetchReview() {
        trailerStateFlow.value = Resource.loading()
        disposable = getReviewMoviesUseCase.execute(_movieId.value,currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                reviewStateFlow.value = Resource.success(res)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    reviewStateFlow.value = Resource.error(it)
                }
            })

    }

    fun fetchNextReviews() {
        currentPage++
        fetchReview()
    }

    fun isFirstPage(): Boolean {
        return currentPage == 1
    }

    fun isLastPage(): Boolean {
        return currentPage == lastPage
    }

    fun updateMovieId(newMovieId: String) {
        _movieId.value = newMovieId
    }

}