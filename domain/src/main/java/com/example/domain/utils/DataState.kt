package com.example.domain.utils

sealed class DataState {

    data class Successful<T>(val result: T) : DataState()

    data class NetworkError(val message: String = Constants.NETWORK_ERROR) : DataState()

    data class Empty(val message: String = Constants.EMPTY_DATA) : DataState()

    data class Failure<T>(val errorResponse: T) : DataState()

    data class Exception(val exception: Throwable) : DataState()

}