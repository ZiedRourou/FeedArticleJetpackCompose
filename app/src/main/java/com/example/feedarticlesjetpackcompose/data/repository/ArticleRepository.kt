package com.example.feedarticlesjetpackcompose.data.repository

import com.example.feedarticlesjetpackcompose.data.api.ApiInterface
import com.example.feedarticlesjetpackcompose.data.dto.request.PostArticleDto
import com.example.feedarticlesjetpackcompose.data.dto.request.UpdateArticleDto
import com.example.feedarticlesjetpackcompose.data.dto.response.ArticleResponseDto
import com.example.feedarticlesjetpackcompose.data.local.AuthSharedPref
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val api: ApiInterface,
    private val authSharedPref: AuthSharedPref
) {

    private val bearerToken ="Bearer ${authSharedPref.getToken()}"

    suspend fun postArticle(
        articleData: PostArticleDto
    ): Resource<Unit> {

        val response = api.postNewArticle(bearerToken,articleData)

        if (response?.code() == 201) {
            response.body()?.let {
                return Resource.Success(
                    data = null,
                    code = response.code()
                )
            }
        }else
            return Resource.Error(response?.code()?: 400)

        return Resource.Error(400)
    }

    suspend fun getAllArticles(): Resource<List<ArticleResponseDto>> {

        val response = api.getAllArticles(bearerToken)

        if (response?.code() == 200) {
            response.body()?.let {
                return Resource.Success(
                    data = it,
                    code = response.code()
                )
            }
        }else
            return Resource.Error(response?.code()?: 400)

        return Resource.Error(400)
    }


    suspend fun deleteArticle(articleId : Int): Resource<Unit> {

        val response = api.deleteArticle(bearerToken, articleId)

        if (response?.code() == 201) {
            response.body()?.let {
                return Resource.Success(
                    data = null,
                    code = response.code()
                )
            }
        }else
            return Resource.Error(response?.code()?: 400)

        return Resource.Error(400)
    }

    suspend fun getArticleById(
        articleId: Int
    ): Resource<ArticleResponseDto> {

        val response = api.getArticleByID(bearerToken, articleId)

        if (response?.code() == 200) {
            response.body()?.let {
                return Resource.Success(
                    data = it,
                    code = response.code()
                )
            }
        }else
            return Resource.Error(response?.code()?: 400)

        return Resource.Error(400)
    }


    suspend fun updateArticle(
        articleId: Int,
        updateArticleDto: UpdateArticleDto
    ): Resource<Unit> {

        val response = api.updateArticle(articleId,bearerToken, updateArticleDto)

        if (response?.code() == 201) {
            response.body()?.let {
                return Resource.Success(
                    data = null,
                    code = response.code()
                )
            }
        }else
            return Resource.Error(response?.code()?: 400)

        return Resource.Error(400)
    }

}
