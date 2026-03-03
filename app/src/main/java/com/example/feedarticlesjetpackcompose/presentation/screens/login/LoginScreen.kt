package com.example.feedarticlesjetpackcompose.presentation.screens.login

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
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {

    val userLoginInfo by viewModel.loginUserInfo.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect("single") {
        viewModel.loginEventSharedFlow.collect {event ->
            when (event) {

                is LoginViewModel.LoginEventState.ShowError ->
                    snackBarHostState.showSnackbar(event.message)

                is LoginViewModel.LoginEventState.RedirectScreen ->
                    navController.navigate(event.screen.route){
                        if(event.screen is Screen.Home)
                            popUpTo(Screen.Login.route) { inclusive = true }
                    }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = {paddingVal ->
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

@Composable
private fun LoginContent(
    paddingValues: PaddingValues,
    userLogin: LoginViewModel.LoginInfoState,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmitForm: () -> Unit,
    onRegisterScreen: ( )->Unit
) {

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {

        FAPrimaryTitle(text = "Connectez-vous")

        Column(
            Modifier.height(150.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            FATextField(
                label = "Login",
                value = userLogin.login,
                onValueChange = onLoginChange,
                isError = userLogin.loginError != null,
                supportingText = userLogin.loginError
            )

            FATextField(
                label = "Mot de passe",
                value = userLogin.password,
                onValueChange = onPasswordChange,
                isPassword = true,
                isError = userLogin.passwordError != null,
                supportingText = userLogin.passwordError,
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
                    text = "Se connecter ",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
            }


            TextButton(onClick = {
                onRegisterScreen()
            }) {
                Text(
                    text = "Pas de compte ? inscrivez vous !",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

    }
}


@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun PreviewLogin() {
    FeedArticlesJetpackComposeTheme {
        LoginContent(
            paddingValues = PaddingValues(),
            userLogin = LoginViewModel.LoginInfoState(
                login = "",
                password = "",
                loginError = null,
                passwordError = null
            ),
            onLoginChange = {},
            onPasswordChange = {},
            onSubmitForm = {},
            onRegisterScreen = {}
        )
    }
}