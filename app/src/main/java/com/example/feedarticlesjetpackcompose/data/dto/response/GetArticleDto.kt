package com.example.feedarticlesjetpackcompose.data.dto.response


import com.squareup.moshi.Json

data class GetArticleDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "titre")
    val title: String,
    @Json(name = "descriptif")
    val content: String,
    @Json(name = "url_image")
    var urlImage: String,
    @Json(name = "categorie")
    val categoryId: Int,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "id_u")
    val userId: Int,
    @Json(name = "is_fav")
    val isFav: Int
)