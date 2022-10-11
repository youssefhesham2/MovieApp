package com.example.domain.repository

import com.example.domain.utils.DataState

interface MoviesRepository {
    /**
     * @param isInternetConnected will be true if the use has an internet connection OR false if Not.
     * @return: if isInternetConnected false will return NetworkError dataState if Not:
     * will be return Successful data state with a list of most popular movies
     * OR Empty data state when get most popular movies response Successfully but with empty movies list
     * OR Failure data state with the error object when failure the request.
     */
    suspend fun getMostPopularMovies(isInternetConnected: Boolean): DataState

    /**
     * Get Top Rated Movies.
     * @param isInternetConnected will be true if the use has an internet connection OR false if Not.
     * @return: if isInternetConnected false will return NetworkError dataState if Not:
     * will be return Successful data state with a list of Top Rated Movies
     * OR Empty data state when get Top Rated Movies response Successfully but with empty movies list
     * OR Failure data state with the error object when failure the request
     */
    suspend fun getTopRatedMovies(isInternetConnected: Boolean): DataState
}