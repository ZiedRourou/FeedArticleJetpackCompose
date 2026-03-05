package com.example.feedarticlesjetpackcompose.presentation.screens.home


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
import com.example.feedarticlesjetpackcompose.utils.navigateForResult


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    val context = LocalContext.current
    val articles by viewModel.homeStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect("single") {
        viewModel.homeEventSharedFlow.collect { event ->
            when (event) {

                is FeedArticleEventState.ShowMessageSnackBar ->
                    snackBarHostState.showSnackbar(context.getString(event.message))

                is FeedArticleEventState.RedirectWithCallbackScreen -> {
                    navController.navigateForResult<Boolean>(
                        route = event.route,
                        navResultCallback = { isFetchResult ->
                            if(isFetchResult){
                                viewModel.fetchArticles()
                            }
                        }
                    )
                }

                is FeedArticleEventState.RedirectScreen -> {
                    navController.navigate(event.screen.route) {
                        if (event.screen is Screen.Login)
                            popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }


                else -> {}
            }
        }
    }

    Scaffold(

        snackbarHost = { SnackbarHost(snackBarHostState) },

        topBar = {
            TopBarHomeContent(
                onAddArticle = viewModel::navigateToAddArticle,
                onLogout = viewModel::logout
            )
        },

        content = { paddingVal ->
            HomeContent(
                paddingVal,
                articles,
                onDelete = viewModel::onDeleteArticle,
                isAuthor = viewModel::isAuthor,
                onClickItem = viewModel::onClickItem,
            )
        },

        bottomBar = {
            BottomBarHome(
                listOptions = articles.categoriesOptions,
                selectedOption = articles.selectedCategory,
                onSelectCategory = viewModel::onSelectCategory
            )
        }
    )
}


