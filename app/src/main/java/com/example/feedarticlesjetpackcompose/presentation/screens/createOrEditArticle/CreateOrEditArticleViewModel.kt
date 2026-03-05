package com.example.feedarticlesjetpackcompose.presentation.screens.createOrEditArticle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.data.dto.request.PostArticleRequestDto
import com.example.feedarticlesjetpackcompose.data.dto.request.UpdateArticleRequestDto
import com.example.feedarticlesjetpackcompose.data.local.AuthSharedPref
import com.example.feedarticlesjetpackcompose.data.repository.ArticleRepository
import com.example.feedarticlesjetpackcompose.utils.Resource
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.presentation.screens.common.FeedArticleEventState
import com.example.feedarticlesjetpackcompose.utils.Category
import com.example.feedarticlesjetpackcompose.utils.categoriesEditOrCreate
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
class CreateOrEditArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val authSharedPref: AuthSharedPref
) : ViewModel() {

    data class CreateArticleInfoState(

        val articleId: Int = 0,
        val title: String = "",
        val content: String = "",
        val imageUrl: String = "",

        val selectedCategory: Category = Category.Diverse,
        val categoriesOptions: ArrayList<Category> = categoriesEditOrCreate,

        val titleError: Int? = null,
        val contentError: Int? = null,

        val editorMode: Boolean = false,
        val isLoading: Boolean = false
    )

    private val _createArticleInfoStateFlow = MutableStateFlow(CreateArticleInfoState())
    val articleFormStateFlow = _createArticleInfoStateFlow.asStateFlow()

    private val _originalArticleStateFlow: MutableStateFlow<UpdateArticleRequestDto?> =
        MutableStateFlow(null)

    private val _createArticleEventSharedFlow = MutableSharedFlow<FeedArticleEventState>()
    val createArticleEventSharedFlow = _createArticleEventSharedFlow.asSharedFlow()


    fun onChangeTitle(title: String) {
        _createArticleInfoStateFlow.update {
            it.copy(
                title = title,
                titleError = if (title.length >= 80) R.string.title_error_max_character else null
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

    fun isAuthor(articleId: Int) {
        if (articleId != 0) {
            _createArticleInfoStateFlow.update {
                it.copy(
                    editorMode = true,
                    articleId = articleId
                )
            }
            fetchArticle(articleId)
        }
    }

    fun submitForm() {
        when {
            !validateArticleData() -> return
            articleFormStateFlow.value.editorMode -> updateArticle()
            else -> postArticle()
        }
    }

    private fun fetchArticle(articleId: Int) {

        viewModelScope.launch {
            _createArticleInfoStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }
            delay(1000)

            val result = withContext(Dispatchers.IO) {
                articleRepository.getArticleById(articleId)
            }

            when (result) {

                is Resource.Success -> {
                    _createArticleInfoStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    result.data?.let { article ->
                        _createArticleInfoStateFlow.update { it ->
                            it.copy(
                                title = article.title,
                                content = article.content,
                                imageUrl = article.urlImage,
                                selectedCategory = categoriesEditOrCreate.first { it.id == article.categoryId }
                            )
                        }
                        _originalArticleStateFlow.value = UpdateArticleRequestDto(
                            id = article.id,
                            title = article.title,
                            content = article.content,
                            imageUrl = article.urlImage,
                            categoryId = categoriesEditOrCreate.first { it.id == article.categoryId }.id
                        )
                    }
                }

                is Resource.Error -> {
                    _createArticleInfoStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _createArticleEventSharedFlow.emit(
                        FeedArticleEventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }


    private fun updateArticle() {

        _createArticleInfoStateFlow.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {

            val updateArticleDto = UpdateArticleRequestDto(
                id = articleFormStateFlow.value.articleId,
                title = articleFormStateFlow.value.title,
                content = articleFormStateFlow.value.content,
                imageUrl = articleFormStateFlow.value.imageUrl,
                categoryId = articleFormStateFlow.value.selectedCategory.id,
            )

            if (updateArticleDto == _originalArticleStateFlow.value) {
                _createArticleEventSharedFlow.emit(
                    FeedArticleEventState.PopBackStackWithResult(false)
                )
                return@launch
            }

            delay(1000)

            val result = withContext(Dispatchers.IO) {
                articleRepository.updateArticle(
                    articleId = articleFormStateFlow.value.articleId,
                    updateArticleDto = updateArticleDto
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
                        FeedArticleEventState.PopBackStackWithResult(true)
                    )
                }

                is Resource.Error -> {
                    _createArticleInfoStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    if (result.code == 422 || result.code == 401) {
                        authSharedPref.clearLogin()
                        _createArticleEventSharedFlow.emit(
                            FeedArticleEventState.ShowMessageSnackBar(R.string.error_server_you_will_be_decconnected)
                        )
                        delay(3000)
                        _createArticleEventSharedFlow.emit(
                            FeedArticleEventState.RedirectScreen(Screen.Login)
                        )
                    }
                    _createArticleEventSharedFlow.emit(
                        FeedArticleEventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }

    private fun postArticle() {

        viewModelScope.launch {
            _createArticleInfoStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }
            delay(1000)

            val result = withContext(Dispatchers.IO) {
                articleRepository.postArticle(
                    articleFormStateFlow.value.let {
                        PostArticleRequestDto(
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
                        FeedArticleEventState.PopBackStackWithResult(true)
                    )
                }

                is Resource.Error -> {
                    _createArticleInfoStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    if (result.code == 422 || result.code == 401) {
                        authSharedPref.clearLogin()
                        _createArticleEventSharedFlow.emit(
                            FeedArticleEventState.ShowMessageSnackBar(R.string.error_server_you_will_be_decconnected)
                        )
                        delay(3000)
                        _createArticleEventSharedFlow.emit(
                            FeedArticleEventState.RedirectScreen(Screen.Login)
                        )
                    }
                }
            }
        }
    }

    private fun validateArticleData(): Boolean {

        val currentArticle = articleFormStateFlow.value

        _createArticleInfoStateFlow.update {
            it.copy(
                titleError = when {
                    currentArticle.title.isEmpty() -> R.string.title_article_field_error_supporting_text
                    currentArticle.title.length >= 80 -> R.string.title_error_max_character
                    else -> null
                },
                contentError = when {
                    currentArticle.content.isEmpty() -> R.string.content_article_field_error_supporting_text
                    else -> null
                },
            )
        }

        currentArticle.let {
            if (it.contentError != null || it.titleError != null)
                return false
        }
        return true
    }
}