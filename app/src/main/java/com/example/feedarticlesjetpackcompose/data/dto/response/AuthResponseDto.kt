package com.example.feedarticlesjetpackcompose.data.dto.response

import com.squareup.moshi.Json

data class AuthResponseDto(
    @Json(name = "id")
    val userId: Int,
    @Json(name = "token")
    val token: String?
)