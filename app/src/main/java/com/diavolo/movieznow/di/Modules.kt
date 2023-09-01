package com.diavolo.movieznow.di

import com.diavolo.data.repository.MoviesLocalRepositoryImpl
import com.diavolo.data.repository.MoviesRemoteRepositoryImpl
import com.diavolo.data.sources.local.mapper.MoviesLocalMapper
import com.diavolo.data.sources.remote.mapper.MoviesRemoteMapper
import com.diavolo.domain.interactor.AddFavoriteMovieUseCase
import com.diavolo.domain.interactor.DeleteFavoriteMovieUseCase
import com.diavolo.domain.interactor.GetFavoriteMovieUseCase
import com.diavolo.domain.interactor.GetFavoriteMoviesUseCase
import com.diavolo.domain.interactor.GetGenreMoviesUseCase
import com.diavolo.domain.interactor.GetMoviesByGenreUseCase
import com.diavolo.domain.interactor.GetReviewMoviesUseCase
import com.diavolo.domain.interactor.GetTrailerMoviesUseCase
import com.diavolo.domain.interactor.UpdateFavoriteMovieUseCase
import com.diavolo.domain.repository.MoviesLocalRepository
import com.diavolo.domain.repository.MoviesRemoteRepository
import com.diavolo.movieznow.ui.details.adapter.ReviewListAdapter
import com.diavolo.movieznow.ui.details.adapter.TrailerListAdapter
import com.diavolo.movieznow.ui.details.viewModel.MovieDetailsViewModel
import com.diavolo.movieznow.ui.favorite.viewModel.FavoriteViewModel
import com.diavolo.movieznow.ui.home.adapter.ImageSliderAdapter
import com.diavolo.movieznow.ui.home.adapter.MovieListAdapter
import com.diavolo.movieznow.ui.home.viewModel.MovieListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { MoviesRemoteMapper() }
    single { MoviesLocalMapper() }
    factory<MoviesRemoteRepository> { MoviesRemoteRepositoryImpl(get()) }
    factory<MoviesLocalRepository> { MoviesLocalRepositoryImpl(androidContext(), get()) }
    factory { MovieListAdapter(androidContext()) }
    factory { TrailerListAdapter(androidContext()) }
    factory { ReviewListAdapter(androidContext()) }
    factory { ImageSliderAdapter(androidContext()) }
}

val movieListModule = module {
    factory { GetMoviesByGenreUseCase(get()) }
    factory { GetGenreMoviesUseCase(get()) }
    viewModel { MovieListViewModel(get(),get()) }
}

val movieDetailModule = module {
    factory { GetReviewMoviesUseCase(get()) }
    factory { GetTrailerMoviesUseCase(get()) }
    factory { AddFavoriteMovieUseCase(get()) }
    factory { GetFavoriteMovieUseCase(get()) }
    factory { DeleteFavoriteMovieUseCase(get()) }
    factory { UpdateFavoriteMovieUseCase(get()) }
    viewModel { MovieDetailsViewModel(get(),get(),get(),get(),get(),get()) }
}

val favoriteMoviesModule = module {
    factory { GetFavoriteMoviesUseCase(get()) }
    viewModel { FavoriteViewModel(get()) }
}


