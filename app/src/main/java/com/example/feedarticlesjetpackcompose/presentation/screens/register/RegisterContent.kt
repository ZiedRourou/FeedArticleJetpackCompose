package com.example.feedarticlesjetpackcompose.presentation.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.feedarticlesjetpackcompose.presentation.components.FAPrimaryTitle
import com.example.feedarticlesjetpackcompose.presentation.components.FATextField
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme


@Composable
fun RegisterContent(
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

        FAPrimaryTitle(text = stringResource(R.string.title_screen_register))

        Column(
            Modifier.height(250.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            FATextField(
                label = stringResource(R.string.label_login_field),
                value = userRegister.login,
                onValueChange = onLoginChange,
                isError = userRegister.loginError != null,
                supportingText = userRegister.loginError
            )

            FATextField(
                label = stringResource(R.string.title_password_field),
                value = userRegister.password,
                onValueChange = onPasswordChange,
                isPassword = true,
                isError = userRegister.passwordError != null,
                supportingText = userRegister.passwordError,
            )
            FATextField(
                label = stringResource(R.string.title_password_confirmation),
                value = userRegister.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                isPassword = true,
                isError = userRegister.confirmPasswordError != null,
                supportingText = userRegister.confirmPasswordError,
            )
        }

        Row(
            modifier = Modifier.height(100.dp).width(150.dp),
        ) {

            FAPrimaryButton(
                onClick = {onSubmitForm() },
                title =stringResource(R.string.title__button_label_register),
                isActive = !userRegister.isLoading
            )
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