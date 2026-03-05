package com.example.feedarticlesjetpackcompose.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun FATextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    label: String,
    isPassword: Boolean = false,
    supportingText: Int? = null,
    minLines : Int? = 1,
    modifier : Modifier? = Modifier,
    singleLine : Boolean? = false
) {

    TextField(
        modifier = modifier ?: Modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        isError = isError,
        visualTransformation =
        if (isPassword)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
        ),
        supportingText = {
            supportingText?.let {
                Text(stringResource(it))
            }
        },
        minLines = minLines ?: 1,
        singleLine = singleLine ?: false
    )
}