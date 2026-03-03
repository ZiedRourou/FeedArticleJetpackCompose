package com.example.feedarticlesjetpackcompose.presentation.screens.home;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpackcompose.data.local.AuthSharedPref
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.presentation.screens.login.LoginViewModel
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authSharedPref: AuthSharedPref
) : ViewModel() {

    sealed class HomeEventState {
        data class ShowError(val message: String) : HomeEventState()
        data class RedirectScreen(val screen: Screen) : HomeEventState()
    }

    private val _homeEventSharedFlow = MutableSharedFlow<HomeEventState>()
    val homeEventSharedFlow = _homeEventSharedFlow.asSharedFlow()

    fun navigateToAddArticle() {
        viewModelScope.launch {
            _homeEventSharedFlow.emit(
                HomeEventState.RedirectScreen(Screen.CreateArticle)
            )
        }
    }

    fun logout() {

        authSharedPref.clearLogin()

        viewModelScope.launch {
            _homeEventSharedFlow.emit(
                HomeEventState.RedirectScreen(Screen.Login)
            )
        }
    }

}