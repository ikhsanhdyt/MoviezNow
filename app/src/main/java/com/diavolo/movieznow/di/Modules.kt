package com.diavolo.movieznow.di

import com.diavolo.data.repository.MoviesRemoteRepositoryImpl
import com.diavolo.data.sources.remote.mapper.MoviesRemoteMapper
import com.diavolo.domain.interactor.GetMoviesByGenreUseCase
import com.diavolo.domain.repository.MoviesRemoteRepository
import com.diavolo.movieznow.ui.home.master.MovieListAdapter
import com.diavolo.movieznow.ui.home.viewModel.MovieListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { MoviesRemoteMapper() }
    factory<MoviesRemoteRepository> { MoviesRemoteRepositoryImpl(get()) }
    factory { MovieListAdapter(androidContext()) }
}

val movieListModule = module {
    factory { GetMoviesByGenreUseCase(get()) }
    viewModel { MovieListViewModel(get()) }
}

