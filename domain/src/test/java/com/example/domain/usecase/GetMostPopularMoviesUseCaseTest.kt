package com.example.domain.usecase

import com.example.domain.repository.MoviesRepository
import com.example.domain.utils.DataState
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetMostPopularMoviesUseCaseTest {
    @Test
    fun `test GetMostPopularMoviesUseCase() then must invoke getMostPopularMovies from MoviesRepository`() {
        //arranges
        var invoked = false
        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                invoked = true
                return DataState.Empty()
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

        }
        val isInternetConnected = true

        // action
        val result =
            runBlocking {
                getMostPopularMoviesUseCaseImpl.invoke(
                    fakeMoviesRepository,
                    isInternetConnected
                )
            }

        //assertion
        Assert.assertTrue(invoked)
    }

    @Test
    fun `test GetTopRatedMoviesUseCase() then must invoke getTopRatedMovies from MoviesRepository`() {
        //arranges
        var invoked = false
        val fakeMoviesRepository = object : MoviesRepository {
            override suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState {
                return DataState.Empty()
            }

            override suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState {
                invoked = true
                return DataState.Empty()
            }

        }
        val isInternetConnected = true

        // action
        val result =
            runBlocking {
                getTopRatedMoviesUseCaseImpl.invoke(
                    fakeMoviesRepository,
                    isInternetConnected
                )
            }

        //assertion
        Assert.assertTrue(invoked)
    }
}