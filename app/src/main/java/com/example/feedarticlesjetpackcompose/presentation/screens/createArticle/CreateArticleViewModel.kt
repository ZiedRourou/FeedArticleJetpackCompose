package com.example.feedarticlesjetpackcompose.presentation.screens.createArticle

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpackcompose.data.dto.request.AuthRequestDto
import com.example.feedarticlesjetpackcompose.data.dto.request.PostArticleDto
import com.example.feedarticlesjetpackcompose.data.local.AuthSharedPref
import com.example.feedarticlesjetpackcompose.data.repository.ArticleRepository
import com.example.feedarticlesjetpackcompose.data.repository.Resource
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.presentation.screens.register.RegisterViewModel
import com.example.feedarticlesjetpackcompose.utils.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val authSharedPref: AuthSharedPref
) : ViewModel() {

    data class CreateArticleInfoState(
        val title: String = "",
        val content: String = "",
        val imageUrl: String = "",
        val selectedCategory: Category = Category.Anime,
        val categoriesOptions: List<Category> = listOf(
            Category.Sport,
            Category.Anime,
            Category.Diverse
        ),

        val titleError: String? = null,
        val contentError: String? = null,
        val selectedCategoryError: String? = null,

        val isLoading: Boolean = false
    )

    sealed class CreateArticleEventState {
        data class ShowError(val message: String) : CreateArticleEventState()
        data class RedirectScreen(val screen: Screen) : CreateArticleEventState()
    }

    private val _createArticleInfoStateFlow = MutableStateFlow(CreateArticleInfoState())
    val articleFormStateFlow = _createArticleInfoStateFlow.asStateFlow()


    private val _createArticleEventSharedFlow = MutableSharedFlow<CreateArticleEventState>()
    val createArticleEventSharedFlow = _createArticleEventSharedFlow.asSharedFlow()


    fun onChangeTitle(title: String) {
        _createArticleInfoStateFlow.update {
            it.copy(
                title = title
            )
        }
    }

    fun onChangeContent(content: String) {
        _createArticleInfoStateFlow.update {
            it.copy(
                content = content
            )
        }
    }

    fun onChangeImageUrl(imageUrl: String) {
        _createArticleInfoStateFlow.update {
            it.copy(
                imageUrl = imageUrl
            )
        }
    }

    fun onSelectCategory(category: Category) {
        _createArticleInfoStateFlow.update {
            it.copy(
                selectedCategory = category
            )
        }
    }


    fun postArticle() {

        if (!validateArticleData()) {
            return
        }

        viewModelScope.launch {

            _createArticleInfoStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                articleRepository.postArticle(
                    articleFormStateFlow.value.let {
                        PostArticleDto(
                            title = it.title,
                            content = it.content,
                            categoryId = it.selectedCategory.id,
                            urlImage = it.imageUrl,
                            userId = authSharedPref.getUserId()
                        )
                    }

                )
            }
            when (result) {

                is Resource.Success -> {
                    _createArticleInfoStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _createArticleEventSharedFlow.emit(
                        CreateArticleEventState.RedirectScreen(Screen.Home)
                    )
                }

                is Resource.Error -> {
                    _createArticleInfoStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    if(result.code == 422 || result.code == 401){
                        authSharedPref.clearLogin()
                        _createArticleEventSharedFlow.emit(
                            CreateArticleEventState.ShowError("vous allez etre déconnecter ")
                        )
                        delay(3000)
                        _createArticleEventSharedFlow.emit(
                            CreateArticleEventState.RedirectScreen(Screen.Login)
                        )
                    }
                    _createArticleEventSharedFlow.emit(
                        CreateArticleEventState.ShowError("Erreur serveur")
                    )

                    Log.e("Login view Model", result.code.toString())
                }
            }
        }
    }

    private fun validateArticleData(): Boolean {

        val currentArticle = articleFormStateFlow.value

        _createArticleInfoStateFlow.update {
            it.copy(
                titleError = when {
                    currentArticle.title.isEmpty() -> "Titre requis"
                    else -> null
                },
                contentError = when {
                    currentArticle.content.isEmpty() -> "Contenu requis"
                    else -> null
                },
            )
        }

        currentArticle.let {
            if (!it.contentError.isNullOrEmpty() || !it.titleError.isNullOrEmpty())
                return false
        }
        return true
    }

}