package com.example.feedarticlesjetpackcompose.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticleBlue
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme


@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.navDirectionsRouteSharedFlow.collect { event ->
            navController.navigate(event.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    SplashContent()
}
