package com.example.feedarticlesjetpackcompose.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feedarticlesjetpackcompose.presentation.screens.createOrEditArticle.CreateArticleScreen
import com.example.feedarticlesjetpackcompose.presentation.screens.createOrEditArticle.CreateOrEditArticleViewModel
import com.example.feedarticlesjetpackcompose.presentation.screens.home.HomeScreen
import com.example.feedarticlesjetpackcompose.presentation.screens.home.HomeViewModel
import com.example.feedarticlesjetpackcompose.presentation.screens.login.LoginScreen
import com.example.feedarticlesjetpackcompose.presentation.screens.login.LoginViewModel
import com.example.feedarticlesjetpackcompose.presentation.screens.register.RegisterScreen
import com.example.feedarticlesjetpackcompose.presentation.screens.register.RegisterViewModel
import com.example.feedarticlesjetpackcompose.presentation.screens.splash.SplashScreen
import com.example.feedarticlesjetpackcompose.presentation.screens.splash.SplashViewModel

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Login : Screen("user_login")
    data object Register : Screen("user_register")
    data object CreateArticle : Screen("new_article")
    data object EditArticle : Screen("edit_article")
}


@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, splashViewModel)
        }

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }

        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(navController, registerViewModel)
        }

        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(navController, homeViewModel)
        }

        composable(Screen.CreateArticle.route) {
            val createArticleViewModel: CreateOrEditArticleViewModel = hiltViewModel()
            CreateArticleScreen(
                navController = navController,
                viewModel = createArticleViewModel,
                0
            )
        }

        composable(Screen.EditArticle.route + "/{articleId}",
            arguments = listOf(
                navArgument("articleId") {
                    type = NavType.IntType
                }
            )) { navBackStack ->

            val articleId = navBackStack.arguments?.getInt("articleId") ?: 0

            val createArticleViewModel: CreateOrEditArticleViewModel = hiltViewModel()
            CreateArticleScreen(
                navController = navController,
                viewModel = createArticleViewModel,
                articleId = articleId
            )
        }
    }
}