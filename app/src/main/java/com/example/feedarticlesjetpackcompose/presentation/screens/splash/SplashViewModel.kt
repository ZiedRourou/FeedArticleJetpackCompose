package com.example.feedarticlesjetpackcompose.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpackcompose.data.local.AuthSharedPref
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authSharedPref: AuthSharedPref
) : ViewModel() {

    private val _navDirectionsRouteSharedFlow = MutableSharedFlow<String>()
    val navDirectionsRouteSharedFlow = _navDirectionsRouteSharedFlow.asSharedFlow()

    init {
        switchNavigation()
    }

    private fun switchNavigation() {
        viewModelScope.launch {
            delay(2000)

            if (authSharedPref.isLogin()) {
                _navDirectionsRouteSharedFlow.emit(Screen.Home.route)
            } else {
                _navDirectionsRouteSharedFlow.emit(Screen.Login.route)
            }
        }
    }
}