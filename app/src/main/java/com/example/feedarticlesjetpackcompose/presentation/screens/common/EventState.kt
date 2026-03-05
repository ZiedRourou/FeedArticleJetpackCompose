package com.example.feedarticlesjetpackcompose.presentation.screens.common

import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen

sealed class FeedArticleEventState {
    data class ShowMessageSnackBar(val message: Int) : FeedArticleEventState()
    data class RedirectScreen(val screen: Screen) : FeedArticleEventState()
    data class RedirectWithCallbackScreen(val route: String) : FeedArticleEventState()
    data class PopBackStackWithResult(val result: Boolean) : FeedArticleEventState()
}