package com.example.data.repository

import android.util.Log
import com.example.data.datasources.remote.MovieApiService
import com.example.data.di.NetworkModule
import com.example.data.extensions.errorResponse
import com.example.data.extensions.resolvePosterPath
import com.example.data.extensions.toDomainEntity
import com.example.data.utils.Constants
import com.example.domain.repository.MoviesRepository
import com.example.domain.utils.DataState


class MoviesRepositoryImpl(private val movieApiService: MovieApiService = NetworkModule.movieApiService) :
    MoviesRepository {
    private val TAG = "MoviesRepositoryImpl"
    override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
        if (!isInternetConnected) return DataState.NetworkError()

        Log.i(TAG, Constants.START_GET_POPULAR_MOVIE_FROM_REMOTE)
        val response = movieApiService.getPopularMovies()
        if (response.isSuccessful) response.body()?.results?.let { movies ->
            Log.i(TAG, Constants.FETCHED_POPULAR_MOVIE_FROM_REMOTE_SUCCESSFULLY)
            if (movies.isNotEmpty()) return DataState.Successful(
                movies.map {
                    it.poster_path = it.poster_path.resolvePosterPath()
                    it.toDomainEntity()
                })
        } ?: return DataState.Empty()

        Log.i(TAG, Constants.FETCHED_POPULAR_MOVIE_FROM_REMOTE_FAILURE)
        return DataState.Failure(response.errorResponse().toDomainEntity())
    }

    override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
        if (!isInternetConnected) return DataState.NetworkError()

        Log.i(TAG, Constants.START_GET_TOP_RATED_MOVIES_FROM_REMOTE)
        val response = movieApiService.getTopRatedMovies()
        if (response.isSuccessful) response.body()?.results?.let { movies ->
            Log.i(TAG, Constants.FETCHED_TOP_RATED_MOVIES_FROM_REMOTE_SUCCESSFULLY)
            if (movies.isNotEmpty()) return DataState.Successful(
                movies.map {
                    it.poster_path = it.poster_path.resolvePosterPath()
                    it.toDomainEntity()
                })
        } ?: return DataState.Empty()

        Log.i(TAG, Constants.FETCHED_TOP_RATED_MOVIES_FROM_REMOTE_FAILURE)
        return DataState.Failure(response.errorResponse().toDomainEntity())
    }
}