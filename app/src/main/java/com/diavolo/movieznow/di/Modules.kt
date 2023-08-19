package com.diavolo.movieznow.di

import com.diavolo.data.repository.MoviesRemoteRepositoryImpl
import com.diavolo.data.sources.remote.mapper.MoviesRemoteMapper
import com.diavolo.domain.interactor.GetGenreMoviesUseCase
import com.diavolo.domain.interactor.GetMoviesByGenreUseCase
import com.diavolo.domain.interactor.GetReviewMoviesUseCase
import com.diavolo.domain.interactor.GetTrailerMoviesUseCase
import com.diavolo.domain.repository.MoviesRemoteRepository
import com.diavolo.movieznow.ui.details.adapter.ReviewListAdapter
import com.diavolo.movieznow.ui.details.adapter.TrailerListAdapter
import com.diavolo.movieznow.ui.details.viewModel.MovieDetailsViewModel
import com.diavolo.movieznow.ui.home.adapter.MovieListAdapter
import com.diavolo.movieznow.ui.home.viewModel.MovieListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { MoviesRemoteMapper() }
    factory<MoviesRemoteRepository> { MoviesRemoteRepositoryImpl(get()) }
    factory { MovieListAdapter(androidContext()) }
    factory { TrailerListAdapter(androidContext()) }
    factory { ReviewListAdapter(androidContext()) }
}

val movieListModule = module {
    factory { GetMoviesByGenreUseCase(get()) }
    factory { GetGenreMoviesUseCase(get()) }
    viewModel { MovieListViewModel(get(),get()) }
}

val movieDetailModule = module {
    factory { GetReviewMoviesUseCase(get()) }
    factory { GetTrailerMoviesUseCase(get()) }
    viewModel { MovieDetailsViewModel(get(),get()) }
}


