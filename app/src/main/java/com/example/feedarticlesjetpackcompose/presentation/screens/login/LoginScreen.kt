package com.example.feedarticlesjetpackcompose.presentation.screens.login


import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticleBlue


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {

    val userLoginInfo by viewModel.loginUserInfo.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect("single") {
        viewModel.loginEventSharedFlow.collect { event ->
            when (event) {

                is LoginViewModel.LoginEventState.ShowError ->
                    snackBarHostState.showSnackbar(event.message)

                is LoginViewModel.LoginEventState.RedirectScreen ->
                    navController.navigate(event.screen.route) {
                        if (event.screen is Screen.Home)
                            popUpTo(Screen.Login.route) { inclusive = true }
                    }
            }
        }
    }
    val strokeWidth = 5.dp

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = { paddingVal ->
            LoginContent(
                paddingVal,
                userLoginInfo,
                onLoginChange = viewModel::onLoginChange,
                onPasswordChange = viewModel::onPasswordChange,
                onSubmitForm = viewModel::loginUser,
                onRegisterScreen = viewModel::onRegisterScreen
            )
        }
    )
}
