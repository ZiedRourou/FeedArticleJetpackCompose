package com.example.feedarticlesjetpackcompose.data.dto.request

import com.squareup.moshi.Json

data class AuthRequestDto (
    @Json(name = "login")
    val login: String,
    @Json(name = "mdp")
    val password: String,
)