package com.example.feedarticlesjetpackcompose.presentation.screens.createOrEditArticle

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.presentation.components.FADisplayImageOrPlaceHolder
import com.example.feedarticlesjetpackcompose.presentation.components.FAPrimaryButton
import com.example.feedarticlesjetpackcompose.presentation.components.FAPrimaryTitle
import com.example.feedarticlesjetpackcompose.presentation.components.FARadioGroupButtonCategories
import com.example.feedarticlesjetpackcompose.presentation.components.FATextField
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme
import com.example.feedarticlesjetpackcompose.utils.Category


@Composable
fun CreateArticleContent(
    paddingValues: PaddingValues,
    articleInfoState: CreateOrEditArticleViewModel.CreateArticleInfoState,
    onSubmitForm: () -> Unit,
    onChangeTitle: (String) -> Unit,
    onChangeContent: (String) -> Unit,
    onChangeImageUrl: (String) -> Unit,
    onSelectCategory: (Category) -> Unit

) {
    var imageCheck by rememberSaveable { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(ScrollState(1), true)
    ) {

        FAPrimaryTitle(
            text = if (articleInfoState.editorMode) stringResource(R.string.title_screen_new_article) else stringResource(
                R.string.title_screen_edit_article
            )
        )

        FATextField(
            label = stringResource(R.string.label_field_article_title),
            value = articleInfoState.title,
            onValueChange = onChangeTitle,
            isError = articleInfoState.titleError != null,
            supportingText = articleInfoState.titleError,
        )

        FATextField(
            label = stringResource(R.string.label_field_article_content),
            value = articleInfoState.content,
            onValueChange = onChangeContent,
            isError = articleInfoState.contentError != null,
            supportingText = articleInfoState.contentError,
            minLines = 5
        )

        FATextField(
            label = stringResource(R.string.label_field_article_image_url),
            value = articleInfoState.imageUrl,
            onValueChange = onChangeImageUrl,
            modifier = Modifier.onFocusChanged {
                imageCheck = articleInfoState.imageUrl
            }
        )

        FADisplayImageOrPlaceHolder(
            image = imageCheck,
            onError = onChangeImageUrl,
            placeholder = R.drawable.feedarticles_logo,
            errorImage = R.drawable.ic_launcher_foreground
        )

        Row {
            FARadioGroupButtonCategories(
                listOptions = articleInfoState.categoriesOptions,
                selectedOption = articleInfoState.selectedCategory,
                onSelectChange = onSelectCategory
            )
        }

        FAPrimaryButton(
            onClick = { onSubmitForm() },
            title = if (articleInfoState.editorMode) stringResource(R.string.label_button_update_article) else stringResource(
                R.string.label_button_publish_article
            ),
            modifier = Modifier.width(250.dp),
            isActive = !articleInfoState.isLoading
        )
    }
}


@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun PreviewCreateArticle() {

    FeedArticlesJetpackComposeTheme {
        CreateArticleContent(
            paddingValues = PaddingValues(),
            onSubmitForm = {},
            articleInfoState = CreateOrEditArticleViewModel.CreateArticleInfoState(
                0,
                "",
                "",
                "",
                Category.Diverse, arrayListOf(Category.Diverse, Category.Anime, Category.Sport)
            ),

            onChangeTitle = { },
            onChangeContent = {},
            onChangeImageUrl = {},
            onSelectCategory = {}
        )
    }
}