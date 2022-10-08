package com.example.movieapp.extensions

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun Application.isInternetConnected(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as (ConnectivityManager)
    if (Build.VERSION.SDK_INT < 23) {
        val networkInfo = connectivityManager.activeNetworkInfo
        networkInfo?.let { return true } ?: return false
    } else {
        val network = connectivityManager.activeNetwork
        network?.let {
            val nc = connectivityManager.getNetworkCapabilities(network)
            return (nc?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true || nc?.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            ) == true)
        }
    }
    return false
}
