package com.example.feedarticlesjetpackcompose.data.api


import com.example.feedarticlesjetpackcompose.data.dto.request.AuthRequestDto
import com.example.feedarticlesjetpackcompose.data.dto.request.PostArticleDto
import com.example.feedarticlesjetpackcompose.data.dto.request.UpdateArticleDto
import com.example.feedarticlesjetpackcompose.data.dto.response.ArticleResponseDto
import com.example.feedarticlesjetpackcompose.data.dto.response.AuthResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {

    @FormUrlEncoded()
    @POST(ApiRoutes.POST_LOGIN)
    suspend fun authLogin(
        @Field("login") userLogin: String,
        @Field("mdp") userPassword: String,
    ): Response<AuthResponseDto>?

    @PUT(ApiRoutes.POST_REGISTER)
    suspend fun authRegister(
        @Body registerDto: AuthRequestDto
    ): Response<AuthResponseDto>?

    @GET(ApiRoutes.GET_ARTICLES)
    suspend fun getAllArticles(
        @Header("Authorization") authorization: String,
    ): Response<List<ArticleResponseDto>>?

    @GET(ApiRoutes.GET_ARTICLE_BY_ID)
    suspend fun getArticleByID(
        @Header("Authorization") authorization: String,
        @Path("id") articleId: Int,
    ): Response<ArticleResponseDto>?

    @PUT(ApiRoutes.PUT_NEW_ARTICLE)
    suspend fun postNewArticle(
        @Header("Authorization") authorization: String,
        @Body article: PostArticleDto
    ): Response<Unit>?

    @POST(ApiRoutes.POST_UPDATE_ARTICLE)
    suspend fun updateArticle(
        @Path("id") articleId: Int,
        @Header("Authorization") authorization: String,
        @Body article: UpdateArticleDto
    ): Response<Unit>?


    @DELETE(ApiRoutes.DELETE_ARTICLE)
    suspend fun deleteArticle(
        @Header("Authorization") authorization: String,
        @Path("id") articleId: Int,
    ): Response<Unit>?

}