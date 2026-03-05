package com.example.feedarticlesjetpackcompose.presentation.screens.home;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.data.dto.response.ArticleResponseDto
import com.example.feedarticlesjetpackcompose.data.local.AuthSharedPref
import com.example.feedarticlesjetpackcompose.data.repository.ArticleRepository
import com.example.feedarticlesjetpackcompose.utils.Resource
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.presentation.screens.common.FeedArticleEventState
import com.example.feedarticlesjetpackcompose.utils.Category
import com.example.feedarticlesjetpackcompose.utils.categoriesHomeFilter
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authSharedPref: AuthSharedPref,
    private val articleRepository: ArticleRepository
) : ViewModel() {

    data class HomeState(
        val filteredArticles: List<ArticleResponseDto>? = null,
        val categoriesOptions: ArrayList<Category> = categoriesHomeFilter,
        val selectedCategory: Category = Category.All,

        val isLoading: Boolean = false
    )

    private val _originalListArticlesStateFlow =
        MutableStateFlow<List<ArticleResponseDto>?>(null  )

    private val _homeStateFlow = MutableStateFlow(HomeState())
    val homeStateFlow = _homeStateFlow.asStateFlow()

    private val _homeEventSharedFlow = MutableSharedFlow<FeedArticleEventState>()
    val homeEventSharedFlow = _homeEventSharedFlow.asSharedFlow()


    init {
        fetchArticles()
    }

    fun navigateToAddArticle() {
        viewModelScope.launch {
            _homeEventSharedFlow.emit(
                FeedArticleEventState.RedirectWithCallbackScreen(Screen.CreateArticle.route)
            )
        }
    }

    fun logout() {

        authSharedPref.clearLogin()
        viewModelScope.launch {
            _homeEventSharedFlow.emit(
                FeedArticleEventState.RedirectScreen(Screen.Login)
            )
        }
    }

    fun isAuthor(authorId: Int) = authSharedPref.getUserId() == authorId

    fun onClickItem(
        currentExpandable: Boolean,
        article: ArticleResponseDto
    ): Boolean {

        if (authSharedPref.getUserId() == article.idUserAuthor) {
            viewModelScope.launch {
                _homeEventSharedFlow.emit(
                    FeedArticleEventState.RedirectWithCallbackScreen("${Screen.EditArticle.route}/${article.id}")
                )
            }
            return false
        }
        return !currentExpandable
    }

    fun onSelectCategory(category: Category) {
        _homeStateFlow.update {
            it.copy(
                selectedCategory = category,
                filteredArticles = _originalListArticlesStateFlow.value?.filter { article ->
                    if (category.id != 0) article.categoryId == category.id else true
                }
            )
        }
    }

    fun fetchArticles() {

        _homeStateFlow.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {

            val result = withContext(Dispatchers.IO) {
                articleRepository.getAllArticles()
            }

            when (result) {

                is Resource.Success -> {
                    _homeStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    result.data?.let { articles ->
                        _homeStateFlow.update {
                            it.copy(
                                filteredArticles = articles,
                            )
                        }
                        _originalListArticlesStateFlow.update { articles }
                    }
                }

                is Resource.Error -> {
                    _homeStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _homeEventSharedFlow.emit(
                        FeedArticleEventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }


    fun onDeleteArticle(articleId: Int) {

        _homeStateFlow.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                articleRepository.deleteArticle(articleId)
            }

            when (result) {

                is Resource.Success -> {
                    _homeStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _homeEventSharedFlow.emit(
                        FeedArticleEventState.ShowMessageSnackBar(R.string.article_delete_message)
                    )
                    fetchArticles()

                }

                is Resource.Error -> {
                    _homeStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _homeEventSharedFlow.emit(
                        FeedArticleEventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }
}