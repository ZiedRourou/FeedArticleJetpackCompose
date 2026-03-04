package com.example.feedarticlesjetpackcompose.presentation.screens.createOrEditArticle

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.presentation.components.FAPrimaryTitle
import com.example.feedarticlesjetpackcompose.presentation.components.FATextField
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.utils.Category

@Composable
fun CreateArticleScreen(
    navController: NavController,
    viewModel: CreateOrEditArticleViewModel,
    articleId : Int,
) {

    viewModel.isAuthor(articleId)

    val articleInfo by viewModel.articleFormStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect("single") {
        viewModel.createArticleEventSharedFlow.collect { event ->
            when (event) {

                is CreateOrEditArticleViewModel.CreateArticleEventState.ShowError ->
                    snackBarHostState.showSnackbar(event.message)

                is CreateOrEditArticleViewModel.CreateArticleEventState.RedirectScreen ->
                    navController.navigate(event.screen.route) {
                        if (event.screen is Screen.Login)
                            popUpTo(Screen.Home.route) { inclusive = true }
                    }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = { paddingVal ->
            CreateArticleContent(
                paddingVal,
                articleInfoState = articleInfo,
                onSubmitForm = viewModel::submitForm,
                onChangeContent = viewModel::onChangeContent,
                onChangeTitle = viewModel::onChangeTitle,
                onChangeImageUrl = viewModel::onChangeImageUrl,
                onSelectCategory = viewModel::onSelectCategory
            )
        }
    )
}

@Composable
private fun CreateArticleContent(
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

        FAPrimaryTitle(text = "Nouvel Article")

        FATextField(
            label = "Titre",
            value = articleInfoState.title,
            onValueChange = onChangeTitle,
            isError = articleInfoState.titleError != null,
            supportingText = articleInfoState.titleError,
        )

        FATextField(
            label = "Contenu",
            value = articleInfoState.content,
            onValueChange = onChangeContent,
            isError = articleInfoState.contentError != null,
            supportingText = articleInfoState.contentError,
            minLines = 5
        )

        FATextField(
            label = "Image URL",
            value = articleInfoState.imageUrl,
            onValueChange = onChangeImageUrl,
            modifier = Modifier.onFocusChanged {
                imageCheck = articleInfoState.imageUrl
            }
        )

        if (imageCheck.isNotEmpty()) {
            AsyncImage(
                model = if (imageCheck.isEmpty()) null else ImageRequest
                    .Builder(LocalContext.current)
                    .data(imageCheck).crossfade(true).build(),
                placeholder = painterResource(id = R.drawable.feedarticles_logo),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                error = painterResource(id = R.drawable.ic_launcher_foreground),
                onError = { onChangeImageUrl("") }
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.feedarticles_logo),
                contentDescription = null,
                Modifier.size(100.dp)
            )
        }


        Row {
            articleInfoState.categoriesOptions.forEach { option ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        RadioButton(
                            selected = articleInfoState.selectedCategory == option,
                            onClick = { onSelectCategory(option) }
                        )
                        Text(option.name, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
        Button(
            onClick = { onSubmitForm() },
            Modifier.width(250.dp)
        ) {
            Text(text = if (articleInfoState.editorMode) "Mettre a jour" else "Enregistrer")
        }

    }
}


@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun PreviewCreateArticle() {
    val options = listOf("Sport", "Manga", "Divers")
    val selectedOption = remember { mutableStateOf(options[0]) }

//    FeedArticlesJetpackComposeTheme {
//        CreateArticleContent(
//            paddingValues = PaddingValues(),
//            onSubmitForm = {},
//            articleInfoState = CreateArticleViewModel.CreateArticleInfoState(
//                "",
//                "",
//                "",
//                Category.Diverse, listOf(Category.Diverse, Category.Anime, Category.Sport)
//            ),
//
//            onChangeTitle = { },
//            onChangeContent = {},
//            onChangeImageUrl = {},
//            onSelectCategory = {}
//        )
//    }
}