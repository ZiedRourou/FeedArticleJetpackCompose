package com.example.feedarticlesjetpackcompose.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme


@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun PreviewLogin() {
    FeedArticlesJetpackComposeTheme {
        FATextField(value = "", onValueChange = {}, label = "uuu")
    }
}

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
    minLines : Int? = 1
) {

    TextField(
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
        minLines = minLines ?: 1
    )

}