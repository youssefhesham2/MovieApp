package com.example.movieapp.ui.frgaments.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.repository.MoviesRepositoryImpl
import com.example.domain.repository.MoviesRepository
import com.example.domain.usecase.GetMostPopularMoviesUseCase
import com.example.domain.usecase.GetTopRatedMoviesUseCase
import com.example.domain.usecase.getMostPopularMoviesUseCaseImpl
import com.example.domain.usecase.getTopRatedMoviesUseCaseImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MoviesViewModelFactory(
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val IODispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val moviesRepository: MoviesRepository = MoviesRepositoryImpl(),
    private val getMostPopularMoviesUseCase: GetMostPopularMoviesUseCase = getMostPopularMoviesUseCaseImpl,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase = getTopRatedMoviesUseCaseImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MoviesViewModel(
        mainDispatcher,
        IODispatcher,
        moviesRepository,
        getMostPopularMoviesUseCase,
        getTopRatedMoviesUseCase
    ) as T
}