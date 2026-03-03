package com.example.feedarticlesjetpackcompose.data.dto.response


import com.squareup.moshi.Json

data class GetArticleByIdDto(
    @Json(name = "status")
    val status: String,
    @Json(name = "article")
    val article: GetArticleDto
)