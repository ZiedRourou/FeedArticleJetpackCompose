package com.example.feedarticlesjetpackcompose.presentation.screens.home;

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpackcompose.data.dto.response.ArticleResponseDto
import com.example.feedarticlesjetpackcompose.data.local.AuthSharedPref
import com.example.feedarticlesjetpackcompose.data.repository.ArticleRepository
import com.example.feedarticlesjetpackcompose.data.repository.Resource
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
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
        val listArticles: List<ArticleResponseDto> =
            listOf(ArticleResponseDto(0, "", "", "", 0, "", 0)),

        val filteredArticles: List<ArticleResponseDto>? = null,
        val categoriesOptions: Array<Category> = categoriesHomeFilter,
        val selectedCategory: Category = Category.All,

        val isLoading: Boolean = false
    )

    sealed class HomeEventState {
        data class ShowError(val message: String) : HomeEventState()
        data class RedirectScreen(val screen: Screen) : HomeEventState()
        data class RedirectEditScreen(val screen: String) : HomeEventState()
    }

    private val _homeStateFlow = MutableStateFlow(HomeState())
    val homeStateFlow = _homeStateFlow.asStateFlow()

    private val _homeEventSharedFlow = MutableSharedFlow<HomeEventState>()
    val homeEventSharedFlow = _homeEventSharedFlow.asSharedFlow()


    init {
        fetchArticles()
    }

    fun navigateToAddArticle() {
        viewModelScope.launch {
            _homeEventSharedFlow.emit(
                HomeEventState.RedirectScreen(Screen.CreateArticle)
            )
        }
    }


    fun logout() {

        authSharedPref.clearLogin()

        viewModelScope.launch {
            _homeEventSharedFlow.emit(
                HomeEventState.RedirectScreen(Screen.Login)
            )
        }
    }

    fun isAuthor(authorId: Int) = authSharedPref.getUserId() == authorId
    fun onClickItem(currentExpandable : Boolean , article: ArticleResponseDto) : Boolean {
        if(authSharedPref.getUserId() == article.idUserAuthor){
            viewModelScope.launch {
                _homeEventSharedFlow.emit(
                    HomeEventState.RedirectEditScreen("${Screen.EditArticle.route}/${article.id}")
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
                filteredArticles = homeStateFlow.value.listArticles.filter { article ->
                    if (category.id != 0) article.categoryId == category.id else true
                }
            )
        }
    }

    private fun fetchArticles() {

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
                                listArticles = articles,
                                filteredArticles = articles
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    _homeStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _homeEventSharedFlow.emit(
                        HomeEventState.ShowError("Erreur serveur")
                    )
                    Log.e("Login view Model", result.code.toString())
                }
            }
        }
    }


    fun onDeleteArticle(articleId: Int) {

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
                        HomeEventState.ShowError("Article supprimé")
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
                        HomeEventState.ShowError("Erreur serveur")
                    )
                    Log.e("Login view Model", result.code.toString())
                }
            }
        }

    }


}