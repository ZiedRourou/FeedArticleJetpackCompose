package com.example.feedarticlesjetpackcompose.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.presentation.components.FAPrimaryButton
import com.example.feedarticlesjetpackcompose.presentation.components.FATextLink
import com.example.feedarticlesjetpackcompose.presentation.components.FAPrimaryTitle
import com.example.feedarticlesjetpackcompose.presentation.components.FATextField
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme


@Composable
fun LoginContent(
    paddingValues: PaddingValues,
    userLogin: LoginViewModel.LoginInfoState,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmitForm: () -> Unit,
    onRegisterScreen: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {

        FAPrimaryTitle(text = stringResource(R.string.title_screen_login))

        Column(
            Modifier.height(150.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            FATextField(
                label = stringResource(R.string.label_login_field),
                value = userLogin.login,
                onValueChange = onLoginChange,
                isError = userLogin.loginError != null,
                supportingText = userLogin.loginError
            )

            FATextField(
                label = stringResource(R.string.title_password_field),
                value = userLogin.password,
                onValueChange = onPasswordChange,
                isPassword = true,
                isError = userLogin.passwordError != null,
                supportingText = userLogin.passwordError,
            )
        }

        Column(
            modifier = Modifier
                .height(100.dp)
                .width(250.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FAPrimaryButton(
                onClick = { onSubmitForm() },
                title = stringResource(R.string.label_button_login),
                modifier = Modifier.width(150.dp),
                isActive = !userLogin.isLoading
            )
            FATextLink(
                onClick = { onRegisterScreen() },
                title = stringResource(R.string.link_redirect_from_login_to_register)
            )
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