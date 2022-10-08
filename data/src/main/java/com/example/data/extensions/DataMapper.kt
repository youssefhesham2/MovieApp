package com.example.data.extensions

import com.example.data.entities.ErrorResponse
import com.example.data.entities.Results
import com.example.domain.entities.ErrorResponseDomainEntity
import com.example.domain.entities.ResultsDomainEntity

fun Results.toDomainEntity() = ResultsDomainEntity(
    id,
    title,
    original_title,
    overview,
    poster_path,
    release_date,
    vote_average
)

fun ErrorResponse.toDomainEntity() = ErrorResponseDomainEntity(status_code, status_message, success)