package com.example.feedarticlesjetpackcompose.data.dto.response


import com.squareup.moshi.Json

data class ResponseApiDto(
    @Json(name = "status")
    val status: Int
)