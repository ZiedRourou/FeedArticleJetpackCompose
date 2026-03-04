package com.example.feedarticlesjetpackcompose.presentation.screens.splash


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen


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

