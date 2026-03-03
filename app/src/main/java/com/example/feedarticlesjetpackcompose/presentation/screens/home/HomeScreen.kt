package com.example.feedarticlesjetpackcompose.presentation.screens.home

import android.graphics.drawable.PaintDrawable
import android.widget.RadioGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.presentation.components.FAPrimaryTitle
import com.example.feedarticlesjetpackcompose.presentation.components.FATextField
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.presentation.screens.login.LoginViewModel
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

//    val userLoginInfo by viewModel.loginUserInfo.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val options = listOf("Tout", "Sport", "Manga", "Divers")
    val selectedOption = remember { mutableStateOf(options[0]) }


    LaunchedEffect("single") {
        viewModel.homeEventSharedFlow.collect {event ->
            when (event) {

                is HomeViewModel.HomeEventState.ShowError ->
                    snackBarHostState.showSnackbar(event.message)

                is HomeViewModel.HomeEventState.RedirectScreen ->{
                    if(event.screen is Screen.Login){
                        navController.navigate(event.screen.route){
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }else
                        navController.navigate(event.screen.route)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopBarHomeContent(
                onAddArticle = viewModel::navigateToAddArticle,
                onLogout = viewModel::logout
            )
        },
        content = { paddingVal ->
            HomeContent(
                paddingVal,
//                userLoginInfo,
//                onLoginChange = viewModel::onLoginChange,
//                onPasswordChange = viewModel::onPasswordChange,
//                onSubmitForm = viewModel::loginUser,
//                onRegisterScreen = viewModel::onRegisterScreen
            )
        },
        bottomBar = { BottomBarHome(listOptions = options, selectedOption =selectedOption )}
    )
}

@Composable
private fun TopBarHomeContent(
    onAddArticle: () -> Unit,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onAddArticle() }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        IconButton(onClick = { onLogout() }) {
            Icon(
                imageVector = Icons.Filled.PowerSettingsNew,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun BottomBarHome(
    listOptions : List<String>,
    selectedOption : MutableState<String>
) {
    Row(
        modifier = Modifier.padding(horizontal = 5.dp)
    ) {

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
}

@Composable
private fun HomeContent(
    paddingValues: PaddingValues,
//    userLogin: LoginViewModel.LoginInfoState,
//    onLoginChange: (String) -> Unit,
//    onPasswordChange: (String) -> Unit,
//    onSubmitForm: () -> Unit,
//    onRegisterScreen: ( )->Unit
) {

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(11) { item ->

                ItemArticle("test", onClickArticle = {})
            }
        }

    }
}

@Composable
private fun ItemArticle(
    titleArticle : String,
    onClickArticle : () -> Unit
){
    Card (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .clickable {
                       onClickArticle()
            }
        ,
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Image(
                painter = painterResource(id = R.drawable.feedarticles_logo),
                contentDescription =null,
                Modifier
                    .size(50.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = titleArticle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

}

@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun PreviewHome() {
    val options = listOf("Tout", "Sport", "Manga", "Divers")
    val selectedOption = remember { mutableStateOf(options[0]) }


    FeedArticlesJetpackComposeTheme {
        Scaffold(
            topBar = {
                TopBarHomeContent(
                    onAddArticle = {},
                    onLogout = {}
                )
            },
            content = { paddingVal ->
                HomeContent(
                    paddingVal,
//                userLoginInfo,
//                onLoginChange = viewModel::onLoginChange,
//                onPasswordChange = viewModel::onPasswordChange,
//                onSubmitForm = viewModel::loginUser,
//                onRegisterScreen = viewModel::onRegisterScreen
                )
            },
            bottomBar = { BottomBarHome(listOptions = options, selectedOption =selectedOption )}
        )
    }
}