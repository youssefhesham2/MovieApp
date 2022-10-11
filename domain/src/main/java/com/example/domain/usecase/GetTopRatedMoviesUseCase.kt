package com.example.domain.usecase

import com.example.domain.repository.MoviesRepository
import com.example.domain.utils.DataState

fun interface GetTopRatedMoviesUseCase {
    suspend fun invoke(moviesRepository: MoviesRepository, isInternetConnected: Boolean): DataState
}

val getTopRatedMoviesUseCaseImpl =
    GetTopRatedMoviesUseCase { moviesRepository: MoviesRepository, isInternetConnected: Boolean ->
        moviesRepository.getTopRatedMovies(isInternetConnected)
    }