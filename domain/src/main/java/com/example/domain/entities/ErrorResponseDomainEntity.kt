package com.example.domain.entities

data class ErrorResponseDomainEntity(
    val status_code: Int,
    val status_message: String,
    val success: Boolean
)