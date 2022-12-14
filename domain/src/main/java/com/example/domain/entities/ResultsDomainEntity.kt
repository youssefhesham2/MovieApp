package com.example.domain.entities

import java.io.Serializable

data class ResultsDomainEntity(
    val id: Int,
    val title: String,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Float,
):Serializable