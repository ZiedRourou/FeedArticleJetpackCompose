package com.example.feedarticlesjetpackcompose.data.dto.response


import com.example.feedarticlesjetpackcompose.data.dto.response.GetArticleDto
import com.squareup.moshi.Json

data class GetArticlesDto(
    @Json(name = "status")
    val status: String,
    @Json(name = "articles")
    val articles: MutableList<GetArticleDto>
)