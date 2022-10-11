package com.example.repository

import com.example.data.datasources.remote.MovieApiService
import com.example.data.entities.Results
import com.example.data.entities.RootResponse
import com.example.data.extensions.toDomainEntity
import com.example.data.repository.MoviesRepositoryImpl
import com.example.domain.utils.DataState
import com.example.utils.UnitTestLogger
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

class MoviesRepositoryImplTest {
    //most Popular Movies
    @Test
    fun `given networkConnection and Successful API response when getPopularMovies() then must return Successfully DataState with list of ResultsDomainEntity`() {
        //arranges
        val movies = ArrayList<Results>()
        movies.add(Results(1, "movie1", "title2", "overview", "", "2022", 5.0))
        movies.add(Results(2, "movie2", "title2", "overview", "", "2022", 5.0))

        val isInternetConnected = true
        val rootResponse = RootResponse(movies)

        val fakeMovieApiService = object : MovieApiService {
            override suspend fun getPopularMovies(apiKey: String): Response<RootResponse> {
                return Response.success(rootResponse)
            }

            override suspend fun getTopRatedMovies(apiKey: String): Response<RootResponse> {
                return Response.success(rootResponse)
            }

        }

        // action
        val result =
            runBlocking {
                MoviesRepositoryImpl(fakeMovieApiService, UnitTestLogger()).getMostPopularMovies(
                    isInternetConnected
                )
            }

        //assertion
        val expected = DataState.Successful(movies.map { it.toDomainEntity() })
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given networkConnection and Successful API response but with empty movies when getPopularMovies() then must return empty DataState`() {
        //arranges
        val movies = ArrayList<Results>()

        val isInternetConnected = true
        val rootResponse = RootResponse(movies)

        val fakeMovieApiService = object : MovieApiService {
            override suspend fun getPopularMovies(apiKey: String): Response<RootResponse> {
                return Response.success(rootResponse)
            }

            override suspend fun getTopRatedMovies(apiKey: String): Response<RootResponse> {
                return Response.success(rootResponse)
            }

        }

        // action
        val result =
            runBlocking {
                MoviesRepositoryImpl(fakeMovieApiService, UnitTestLogger()).getMostPopularMovies(
                    isInternetConnected
                )
            }

        //assertion
        val expected = DataState.Empty()
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given networkConnection and Successful API response but with null response body when getPopularMovies() then must return Exception DataState`() {
        //arranges
        val isInternetConnected = true

        val fakeMovieApiService = object : MovieApiService {
            override suspend fun getPopularMovies(apiKey: String): Response<RootResponse> {
                return Response.success(null)
            }

            override suspend fun getTopRatedMovies(apiKey: String): Response<RootResponse> {
                return Response.success(null)
            }

        }

        // action
        val result =
            runBlocking {
                MoviesRepositoryImpl(fakeMovieApiService, UnitTestLogger()).getMostPopularMovies(
                    isInternetConnected
                )
            }

        //assertion
        val expected = DataState.Exception(NullPointerException())
        Assert.assertEquals(expected.javaClass, result.javaClass)
    }

    @Test
    fun `given networkConnection with false when getPopularMovies() then must return NetworkError DataState`() {
        //arranges
        val isInternetConnected = false
        val fakeMovieApiService = object : MovieApiService {
            override suspend fun getPopularMovies(apiKey: String): Response<RootResponse> {
                return Response.success(null)
            }

            override suspend fun getTopRatedMovies(apiKey: String): Response<RootResponse> {
                return Response.success(null)
            }

        }

        // action
        val result =
            runBlocking {
                MoviesRepositoryImpl(fakeMovieApiService, UnitTestLogger()).getMostPopularMovies(
                    isInternetConnected
                )
            }

        //assertion
        val expected = DataState.NetworkError()
        Assert.assertEquals(expected, result)
    }

    // Top rated Movies
    @Test
    fun `given networkConnection and Successful API response when getTopRatedMovies() then must return Successfully DataState with list of ResultsDomainEntity`() {
        //arranges
        val topRatedMovies = ArrayList<Results>()
        topRatedMovies.add(Results(1, "movie1", "title2", "overview", "", "2022", 5.0))
        topRatedMovies.add(Results(2, "movie2", "title2", "overview", "", "2022", 5.0))

        val isInternetConnected = true
        val rootResponse = RootResponse(topRatedMovies)

        val fakeMovieApiService = object : MovieApiService {
            override suspend fun getPopularMovies(apiKey: String): Response<RootResponse> {
                return Response.success(null)
            }

            override suspend fun getTopRatedMovies(apiKey: String): Response<RootResponse> {
                return Response.success(rootResponse)
            }

        }

        // action
        val result =
            runBlocking {
                MoviesRepositoryImpl(fakeMovieApiService, UnitTestLogger()).getTopRatedMovies(
                    isInternetConnected
                )
            }

        //assertion
        val expected = DataState.Successful(topRatedMovies.map { it.toDomainEntity() })
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given networkConnection and Successful API response but with empty movies when getTopRatedMovies() then must return empty DataState`() {
        //arranges
        val topRatedMovies = ArrayList<Results>()

        val isInternetConnected = true
        val rootResponse = RootResponse(topRatedMovies)

        val fakeMovieApiService = object : MovieApiService {
            override suspend fun getPopularMovies(apiKey: String): Response<RootResponse> {
                return Response.success(null)
            }

            override suspend fun getTopRatedMovies(apiKey: String): Response<RootResponse> {
                return Response.success(rootResponse)
            }

        }

        // action
        val result =
            runBlocking {
                MoviesRepositoryImpl(fakeMovieApiService, UnitTestLogger()).getTopRatedMovies(
                    isInternetConnected
                )
            }

        //assertion
        val expected = DataState.Empty()
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given networkConnection and Successful API response but with null response body when getTopRatedMovies() then must return Exception DataState`() {
        //arranges
        val isInternetConnected = true

        val fakeMovieApiService = object : MovieApiService {
            override suspend fun getPopularMovies(apiKey: String): Response<RootResponse> {
                return Response.success(null)
            }

            override suspend fun getTopRatedMovies(apiKey: String): Response<RootResponse> {
                return Response.success(null)
            }

        }

        // action
        val result =
            runBlocking {
                MoviesRepositoryImpl(fakeMovieApiService, UnitTestLogger()).getTopRatedMovies(
                    isInternetConnected
                )
            }

        //assertion
        val expected = DataState.Exception(NullPointerException())
        Assert.assertEquals(expected.javaClass, result.javaClass)
    }

    @Test
    fun `given networkConnection with false when getTopRatedMovies() then must return NetworkError DataState`() {
        //arranges
        val isInternetConnected = false
        val fakeMovieApiService = object : MovieApiService {
            override suspend fun getPopularMovies(apiKey: String): Response<RootResponse> {
                return Response.success(null)
            }

            override suspend fun getTopRatedMovies(apiKey: String): Response<RootResponse> {
                return Response.success(null)
            }

        }

        // action
        val result =
            runBlocking {
                MoviesRepositoryImpl(fakeMovieApiService, UnitTestLogger()).getTopRatedMovies(
                    isInternetConnected
                )
            }

        //assertion
        val expected = DataState.NetworkError()
        Assert.assertEquals(expected, result)
    }
}