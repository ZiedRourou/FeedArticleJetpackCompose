package com.example.feedarticlesjetpackcompose.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme


@Composable
fun FAPrimaryTitle(
    text: String
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun FATextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    label: String,
    isPassword: Boolean = false,
    supportingText: String? = null,
    minLines : Int? = 1,
    maxLines : Int? = 5,
    modifier : Modifier? = Modifier
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
                Text(it)
            }
        },
        minLines = minLines ?: 1,
        maxLines = maxLines ?:5
    )

}