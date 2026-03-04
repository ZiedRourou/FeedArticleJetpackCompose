package com.example.feedarticlesjetpackcompose.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.feedarticlesjetpackcompose.utils.Category

@Composable
fun FAPrimaryButton(
    onClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    isActive: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,

            ),
        modifier = modifier.fillMaxWidth(),
        enabled = isActive

    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun FARadioGroupButtonCategories(
    listOptions: ArrayList<Category>,
    selectedOption: Category,
    onSelectChange: (Category) -> Unit
) {
    listOptions.forEach { option ->
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,

                ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { onSelectChange(option) }
                )
                Text(stringResource(id = option.name), style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
