package com.example.feedarticlesjetpackcompose.presentation.screens.register


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
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    val userRegisterInfo by viewModel.registerUserInfo.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect("single") {
        viewModel.registerEventSharedFlow.collect { event ->
            when (event) {

                is RegisterViewModel.RegisterEventState.ShowError ->
                    snackBarHostState.showSnackbar(event.message)

                is RegisterViewModel.RegisterEventState.RedirectScreen ->
                    navController.navigate(event.screen.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = { paddingVal ->
            RegisterContent(
                paddingVal,
                userRegisterInfo,
                onLoginChange = viewModel::onLoginChange,
                onPasswordChange = viewModel::onPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onSubmitForm = viewModel::registerUser,
            )
        }
    )
}