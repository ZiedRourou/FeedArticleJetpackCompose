package com.example.feedarticlesjetpackcompose.data.dto.request


import com.squareup.moshi.Json

data class UpdateArticleDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "desc")
    val content: String,
    @Json(name = "image")
    val imageUrl: String,
    @Json(name = "cat")
    val categoryId: Int,
)