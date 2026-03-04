package com.example.feedarticlesjetpackcompose.presentation.screens.home


import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    val articles by viewModel.homeStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect("single") {
        viewModel.homeEventSharedFlow.collect { event ->
            when (event) {

                is HomeViewModel.HomeEventState.ShowError ->
                    snackBarHostState.showSnackbar(event.message)

                is HomeViewModel.HomeEventState.RedirectScreen -> {
                    if (event.screen is Screen.Login) {
                        navController.navigate(event.screen.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    } else
                        navController.navigate(event.screen.route)
                }

                is HomeViewModel.HomeEventState.RedirectEditScreen -> {
                    navController.navigate(event.screen)
                }
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
                onClickItem = viewModel::onClickItem
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


