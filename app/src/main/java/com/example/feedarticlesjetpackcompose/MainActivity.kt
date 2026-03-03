package com.example.feedarticlesjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.feedarticlesjetpackcompose.presentation.navigation.AppNavHost
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FeedArticlesJetpackComposeTheme {
                AppNavHost()
            }
        }
    }
}
