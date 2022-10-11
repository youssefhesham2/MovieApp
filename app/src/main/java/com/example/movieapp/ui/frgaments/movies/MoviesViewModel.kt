package com.example.movieapp.ui.frgaments.movies

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.MoviesRepositoryImpl
import com.example.domain.entities.ErrorResponseDomainEntity
import com.example.domain.entities.ResultsDomainEntity
import com.example.domain.repository.MoviesRepository
import com.example.domain.usecase.GetMostPopularMoviesUseCase
import com.example.domain.usecase.GetTopRatedMoviesUseCase
import com.example.domain.usecase.getMostPopularMoviesUseCaseImpl
import com.example.domain.usecase.getTopRatedMoviesUseCaseImpl
import com.example.domain.utils.DataState
import com.example.movieapp.utils.Constants
import com.example.movieapp.utils.LoadingState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MoviesViewModel(
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val IODispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val moviesRepository: MoviesRepository = MoviesRepositoryImpl(),
    private val getMostPopularMoviesUseCase: GetMostPopularMoviesUseCase = getMostPopularMoviesUseCaseImpl,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase = getTopRatedMoviesUseCaseImpl
) : ViewModel() {
    private val TAG = "MoviesViewModel"
    val loading = MutableLiveData<LoadingState>()
    val movies = MutableLiveData<List<ResultsDomainEntity>>()
    val edgeCase = MutableLiveData<String>()
    val failure = MutableLiveData<ErrorResponseDomainEntity>()
    val exception = MutableLiveData<Throwable>()
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e(TAG, "🤬 Exception $throwable in context:$coroutineContext")
            loading.postValue(LoadingState.DISMISS)
            exception.postValue(throwable)
        }

    fun fetchMostPopularMovies(isInternetConnected: Boolean) {
        loading.postValue(LoadingState.SHOW)
        viewModelScope.launch(coroutineExceptionHandler) {
            val response =
                withContext(IODispatcher) {
                    getMostPopularMoviesUseCase.invoke(moviesRepository, isInternetConnected)
                }
            handleGetMoviesResponse(response)
        }
    }

    fun fetchTopRatedMovies(isInternetConnected: Boolean) {
        loading.postValue(LoadingState.SHOW)
        viewModelScope.launch(coroutineExceptionHandler) {
            val response =
                withContext(IODispatcher) {
                    getTopRatedMoviesUseCase.invoke(moviesRepository, isInternetConnected)
                }
            handleGetMoviesResponse(response)
        }
    }

    private fun handleGetMoviesResponse(response: DataState) {
        loading.postValue(LoadingState.DISMISS)
        when (response) {
            is DataState.Successful<*> -> movies.postValue(response.result as List<ResultsDomainEntity>)
            is DataState.Empty -> edgeCase.postValue(response.message)
            is DataState.Failure<*> -> failure.postValue(response.errorResponse as ErrorResponseDomainEntity)
            else -> edgeCase.postValue(Constants.EMPTY)
        }
    }
}