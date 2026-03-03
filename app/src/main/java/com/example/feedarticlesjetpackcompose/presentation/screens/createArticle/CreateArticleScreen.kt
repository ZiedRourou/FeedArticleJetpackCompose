package com.example.feedarticlesjetpackcompose.presentation.screens.createArticle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.presentation.components.FAPrimaryTitle
import com.example.feedarticlesjetpackcompose.presentation.components.FATextField
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.presentation.screens.register.RegisterViewModel
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme

@Composable
fun CreateArticleScreen(
    navController: NavController,
    viewModel: CreateArticleViewModel
){
//    val userRegisterInfo by viewModel.registerUserInfo.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val options = listOf("Sport", "Manga", "Divers")
    val selectedOption = remember { mutableStateOf(options[0]) }
//    LaunchedEffect("single") {
//        viewModel.registerEventSharedFlow.collect { event ->
//            when (event) {
//
//                is RegisterViewModel.RegisterEventState.ShowError ->
//                    snackBarHostState.showSnackbar(event.message)
//
//                is RegisterViewModel.RegisterEventState.RedirectScreen ->
//                    navController.navigate(event.screen.route){
//                        popUpTo(Screen.Login.route) { inclusive = true }
//                    }
//            }
//        }
//    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = { paddingVal ->
            CreateArticleContent(
                paddingVal,
                onSubmitForm = {},
                listOptions = options,
                selectedOption = selectedOption
            )
        }
    )
}

@Composable
private fun CreateArticleContent(
    paddingValues: PaddingValues,
    onSubmitForm : () -> Unit,
    listOptions : List<String>,
    selectedOption : MutableState<String>
) {

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {

        FAPrimaryTitle(text = "Nouvel Article")

        FATextField(
            value = "",
            onValueChange ={} ,
            label ="Titre"
        )

        FATextField(
            value = "",
            onValueChange ={},
            minLines = 5,
            label ="Contenu"
        )

        FATextField(
            value = "",
            onValueChange ={} ,
            label ="Image URL"
        )

        Image(
            painter = painterResource(id = R.drawable.feedarticles_logo),
            contentDescription =null,
            Modifier
                .size(100.dp),
        )


        Row {
            listOptions.forEach { option ->
                Row (
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        RadioButton(
                            selected = selectedOption.value == option,
                            onClick = { selectedOption.value = option }
                        )
                        Text(option, style = MaterialTheme.typography.bodyLarge)
                    }

                }

            }
        }

        Button(
            onClick = { onSubmitForm() },
            Modifier.width(250.dp)
        ) {
            Text(text = "Enregister")
        }




    }
}


@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun PreviewCreateArticle() {
    val options = listOf("Sport", "Manga", "Divers")
    val selectedOption = remember { mutableStateOf(options[0]) }
    
    FeedArticlesJetpackComposeTheme {
        CreateArticleContent(
            paddingValues = PaddingValues(),
            onSubmitForm = {},
            listOptions = options,
            selectedOption = selectedOption
        )
    }
}