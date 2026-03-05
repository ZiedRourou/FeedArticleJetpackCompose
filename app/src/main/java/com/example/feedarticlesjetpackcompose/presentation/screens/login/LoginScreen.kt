package com.example.feedarticlesjetpackcompose.presentation.screens.login


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


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val context = LocalContext.current

    val userLoginState by viewModel.loginUserStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect("single") {
        viewModel.loginEventSharedFlow.collect { event ->
            when (event) {

                is FeedArticleEventState.ShowMessageSnackBar ->
                    snackBarHostState.showSnackbar(context.getString(event.message))

                is FeedArticleEventState.RedirectScreen ->
                    navController.navigate(event.screen.route) {
                        if (event.screen is Screen.Home)
                            popUpTo(Screen.Login.route) { inclusive = true }
                    }

                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = { paddingVal ->
            LoginContent(
                paddingVal,
                userLoginState,
                onLoginChange = viewModel::onLoginChange,
                onPasswordChange = viewModel::onPasswordChange,
                onSubmitForm = viewModel::loginUser,
                onRegisterScreen = viewModel::onRegisterScreen
            )
        }
    )
}
