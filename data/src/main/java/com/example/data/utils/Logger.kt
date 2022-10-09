package com.example.data.utils

import android.util.Log

open class Logger {

    open fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    open fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    open fun v(tag: String, message: String) {
        Log.v(tag, message)
    }

    open fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    open fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}