package com.example.data.extensions

import com.example.data.entities.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response
import retrofit2.Retrofit

fun Response<*>.errorResponse(): ErrorResponse {
    return Gson().fromJson(this.errorBody()!!.charStream(), ErrorResponse::class.java)
}