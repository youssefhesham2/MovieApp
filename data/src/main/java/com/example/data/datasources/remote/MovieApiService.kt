package com.example.data.datasources.remote

import com.example.data.BuildConfig
import com.example.data.entities.RootResponse
import com.example.data.utils.Constants
import com.example.data.utils.EndPoints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET(EndPoints.GET_POPULAR_MOVIES)
    suspend fun getPopularMovies(@Query(Constants.QUERY_PARAMS_API_KEY) apiKey: String = BuildConfig.API_KEY): Response<RootResponse>

    @GET(EndPoints.GET_TOP_RATED_MOVIES)
    suspend fun getTopRatedMovies(@Query(Constants.QUERY_PARAMS_API_KEY) apiKey: String = BuildConfig.API_KEY): Response<RootResponse>
}