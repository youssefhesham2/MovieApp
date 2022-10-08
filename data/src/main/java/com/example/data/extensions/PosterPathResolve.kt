package com.example.data.extensions

import com.example.data.BuildConfig
import com.example.data.utils.Constants

fun String.resolvePosterPath(): String {
    return BuildConfig.BASE_IMAGE_PATH + Constants.POSTER_SIZE + this
}