package com.diavolo.movieznow.ui.details.viewModel

import androidx.lifecycle.ViewModel
import com.diavolo.domain.interactor.AddFavoriteMovieUseCase
import com.diavolo.domain.interactor.DeleteFavoriteMovieUseCase
import com.diavolo.domain.interactor.GetFavoriteMovieUseCase
import com.diavolo.domain.interactor.GetReviewMoviesUseCase
import com.diavolo.domain.interactor.GetTrailerMoviesUseCase
import com.diavolo.domain.interactor.UpdateFavoriteMovieUseCase
import com.diavolo.model.Movie
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
    private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
    private val getFavoriteMovieUseCase: GetFavoriteMovieUseCase,
    private val deleteFavoriteMovieUseCase: DeleteFavoriteMovieUseCase,
    private val updateFavoriteMovieUseCase: UpdateFavoriteMovieUseCase
) : ViewModel() {
    private var currentPage = 1
    private var lastPage = 1

    private val trailerStateFlow = MutableStateFlow<Resource<TrailerResponse>>(Resource.empty())
    private val reviewStateFlow = MutableStateFlow<Resource<ReviewMoviesResponse>>(Resource.empty())
    private val favoritesStateFlow = MutableStateFlow<Resource<Boolean>>(Resource.empty())

    private var disposable: Disposable? = null
    val trailerMovieState: StateFlow<Resource<TrailerResponse>>
        get() = trailerStateFlow

    val reviewState: StateFlow<Resource<ReviewMoviesResponse>>
        get() = reviewStateFlow

    private val _movieId = MutableStateFlow<String>("")
    val movieId: StateFlow<String> get() = _movieId

    val favoritesState: StateFlow<Resource<Boolean>>
        get() = favoritesStateFlow

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
        disposable = getReviewMoviesUseCase.execute(_movieId.value, currentPage)
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
    fun fetchFavoriteMovieState(movie: Movie) {
        favoritesStateFlow.value = Resource.loading()

        disposable = getFavoriteMovieUseCase.execute(movie.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // favorite movie exists, update data first
                updateFavoriteMovie(movie)
                favoritesStateFlow.value = Resource.success(true)
            }, {
                // favorite movie does not exist
                favoritesStateFlow.value = Resource.success(false)
            })
    }


    fun toggleFavorite(movie: Movie) {
        favoritesStateFlow.value = Resource.loading()

        disposable = getFavoriteMovieUseCase.execute(movie.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // favorite movie exists, remove from favorites
                deleteFavoriteMovie(movie)
            }, {
                // favorite movie does not exist, add to favorites
                addFavoriteMovie(movie)
            })
    }

    fun addFavoriteMovie(movie: Movie) {
        favoritesStateFlow.value = Resource.loading()

        disposable = addFavoriteMovieUseCase.execute(movie)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                favoritesStateFlow.value = Resource.success(true)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    favoritesStateFlow.value = Resource.error(it)
                }
            })
    }

    fun deleteFavoriteMovie(movie: Movie) {
        favoritesStateFlow.value = Resource.loading()

        disposable = deleteFavoriteMovieUseCase.execute(movie)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                favoritesStateFlow.value = Resource.success(false)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    favoritesStateFlow.value = Resource.error(it)
                }
            })
    }

    fun updateFavoriteMovie(movie: Movie) {
        favoritesStateFlow.value = Resource.loading()

        disposable = updateFavoriteMovieUseCase.execute(movie)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                favoritesStateFlow.value = Resource.success(true)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    favoritesStateFlow.value = Resource.error(it)
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