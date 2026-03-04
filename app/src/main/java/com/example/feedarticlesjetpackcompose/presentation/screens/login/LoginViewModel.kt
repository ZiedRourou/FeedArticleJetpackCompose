package com.example.feedarticlesjetpackcompose.presentation.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpackcompose.data.dto.request.AuthRequestDto
import com.example.feedarticlesjetpackcompose.data.local.AuthSharedPref
import com.example.feedarticlesjetpackcompose.data.repository.AuthRepository
import com.example.feedarticlesjetpackcompose.utils.Resource
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.utils.STRONG_PASSWORD_REGEX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authSharedPref: AuthSharedPref
) : ViewModel() {

    data class LoginInfoState(
        val login: String = "",
        val password: String = "",

        val loginError: String? = null,
        val passwordError: String? = null,

        val isLoading: Boolean = false
    )

    sealed class LoginEventState {
        data class ShowError(val message: String) : LoginEventState()
        data class RedirectScreen(val screen: Screen) : LoginEventState()
    }


    private val _loginUserInfo = MutableStateFlow(LoginInfoState())
    val loginUserInfo = _loginUserInfo.asStateFlow()

    private val _loginEventSharedFlow = MutableSharedFlow<LoginEventState>()
    val loginEventSharedFlow = _loginEventSharedFlow.asSharedFlow()


    fun onLoginChange(login: String) {
        _loginUserInfo.update {
            it.copy(
                login = login
            )
        }
    }

    fun onPasswordChange(password: String) {
        _loginUserInfo.update {
            it.copy(
                password = password
            )
        }
    }

    fun onRegisterScreen() {
        viewModelScope.launch {
            _loginEventSharedFlow.emit(
                LoginEventState.RedirectScreen(Screen.Register)
            )
        }
    }

    fun loginUser() {

        if (!validateLoginData())
            return

        viewModelScope.launch {

            _loginUserInfo.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                authRepository.loginUser(
                    AuthRequestDto(
                        login = loginUserInfo.value.login,
                        password = loginUserInfo.value.password,

                        )
                )
            }
            when (result) {

                is Resource.Success -> {
                    _loginUserInfo.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    result.data?.let {
                        if (it.token != null) {
                            authSharedPref.saveUserInfo(
                                token = it.token,
                                userId = it.userId,
                            )
                        }
                        _loginEventSharedFlow.emit(
                            LoginEventState.RedirectScreen(Screen.Home)
                        )
                    }
                }

                is Resource.Error -> {
                    _loginUserInfo.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    val message = when (result.code) {
                        401 -> "user inconnu (mauvais login/mdp)"
                        else -> "Erreur serveur"
                    }

                    _loginEventSharedFlow.emit(
                        LoginEventState.ShowError(message)
                    )

                    if (result.code != 401)
                        Log.e("Login view Model", result.code.toString())
                }
            }
        }
    }


    private fun validateLoginData(): Boolean {

        var isValid = false

        _loginUserInfo.let { loginData ->
            when {
                loginData.value.login.isBlank() ->
                    loginData.update { it.copy(loginError ="Login requis") }

                loginData.value.password.isBlank() ->
                    loginData.update { it.copy(passwordError = "Mot de passe requis")}

                !Regex(STRONG_PASSWORD_REGEX).matches(loginData.value.password) ->
                    isValid = false

                else -> {
                    isValid = true
                }
            }
        }

        return isValid
    }


}

