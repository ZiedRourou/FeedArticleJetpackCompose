package com.example.feedarticlesjetpackcompose.presentation.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feedarticlesjetpackcompose.presentation.components.FAPrimaryTitle
import com.example.feedarticlesjetpackcompose.presentation.components.FATextField
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.presentation.screens.login.LoginViewModel
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme

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
                    navController.navigate(event.screen.route){
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

@Composable
private fun RegisterContent(
    paddingValues: PaddingValues,
    userRegister: RegisterViewModel.RegisterInfoState,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmitForm: () -> Unit,
) {

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {

        FAPrimaryTitle(text = "Nouveau Compte")

        Column(
            Modifier.height(250.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            FATextField(
                label = "Login",
                value = userRegister.login,
                onValueChange = onLoginChange,
                isError = userRegister.loginError != null,
                supportingText = userRegister.loginError
            )

            FATextField(
                label = "Mot de passe",
                value = userRegister.password,
                onValueChange = onPasswordChange,
                isPassword = true,
                isError = userRegister.passwordError != null,
                supportingText = userRegister.passwordError,
            )
            FATextField(
                label = "Mot de passe",
                value = userRegister.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                isPassword = true,
                isError = userRegister.confirmPasswordError != null,
                supportingText = userRegister.confirmPasswordError,
            )

        }

        Column(
            modifier = Modifier.height(100.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    onSubmitForm()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Text(
                    text = "S'inscrire ",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }

    }
}


@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun PreviewRegister() {
    FeedArticlesJetpackComposeTheme {
        RegisterContent(
            paddingValues = PaddingValues(),
            userRegister = RegisterViewModel.RegisterInfoState(
                login = "",
                password = "",
                loginError = null,
                passwordError = null
            ),
            onLoginChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange  = {},
            onSubmitForm = {},
        )
    }
}