package com.example.data.di

import com.example.data.BuildConfig
import com.example.data.datasources.remote.MovieApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    val movieApiService: MovieApiService by lazy {
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(MovieApiService::class.java)
    }
}