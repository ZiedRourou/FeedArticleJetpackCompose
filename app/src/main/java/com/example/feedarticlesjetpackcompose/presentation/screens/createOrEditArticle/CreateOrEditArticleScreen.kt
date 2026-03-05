package com.example.feedarticlesjetpackcompose.presentation.screens.createOrEditArticle


import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.presentation.screens.common.FeedArticleEventState
import com.example.feedarticlesjetpackcompose.utils.popBackStackWithResult

@Composable
fun CreateArticleScreen(
    navController: NavController,
    viewModel: CreateOrEditArticleViewModel,
    articleId : Int,
) {

    viewModel.isAuthor(articleId)

    val context = LocalContext.current

    val articleInfo by viewModel.articleFormStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect("single") {
        viewModel.createArticleEventSharedFlow.collect { event ->
            when (event) {

                is FeedArticleEventState.ShowMessageSnackBar ->
                    snackBarHostState.showSnackbar(context.getString(event.message))

                is FeedArticleEventState.RedirectScreen ->
                    navController.navigate(event.screen.route) {
                        if (event.screen is Screen.Login)
                            popUpTo(Screen.Home.route) { inclusive = true }
                    }
                is FeedArticleEventState.PopBackStackWithResult ->
                    navController.popBackStackWithResult(event.result)

                else -> {}
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
