package com.example.feedarticlesjetpackcompose.data.dto.request


import com.squareup.moshi.Json

data class PostArticleRequestDto(
    @Json(name = "title")
    val title: String,
    @Json(name = "desc")
    val content: String,
    @Json(name = "image")
    val urlImage: String,
    @Json(name = "cat")
    val categoryId: Int,
    @Json(name = "id_u")
    var userId: Int ,
)

