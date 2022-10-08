package com.example.domain.usecase

import com.example.domain.repository.MoviesRepository
import com.example.domain.utils.DataState

fun interface GetMostPopularMoviesUseCase {
    suspend fun invoke(moviesRepository: MoviesRepository, isInternetConnected: Boolean): DataState
}

val getMostPopularMoviesUseCaseImpl =
    GetMostPopularMoviesUseCase { moviesRepository: MoviesRepository, isInternetConnected: Boolean ->
        moviesRepository.getMostPopularMovies(isInternetConnected)
    }