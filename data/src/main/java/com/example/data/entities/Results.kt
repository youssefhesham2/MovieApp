package com.example.data.entities

data class Results(
    val id: Int,
    val title: String,
    val original_title: String,
    val overview: String,
    var poster_path: String,
    val release_date: String,
    val vote_average: Double,
)