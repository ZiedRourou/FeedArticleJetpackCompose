package com.example.feedarticlesjetpackcompose.data.dto.response


import com.squareup.moshi.Json

data class ArticleResponseDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "titre")
    val title: String,
    @Json(name = "descriptif")
    val content: String,
    @Json(name = "url_image")
    val urlImage: String,
    @Json(name = "categorie")
    val categoryId: Int,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "id_u")
    val idUserAuthor: Int
)

