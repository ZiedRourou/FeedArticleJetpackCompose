package com.example.feedarticlesjetpackcompose.presentation.screens.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpackcompose.data.dto.request.AuthRequestDto
import com.example.feedarticlesjetpackcompose.data.local.AuthSharedPref
import com.example.feedarticlesjetpackcompose.data.repository.AuthRepository
import com.example.feedarticlesjetpackcompose.data.repository.Resource
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.utils.STRONG_PASSWORD
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
class RegisterViewModel @Inject constructor(
    private val authSharedPref: AuthSharedPref,
    private val authRepository: AuthRepository
) : ViewModel() {
    data class RegisterInfoState(
        val login: String = "",
        val password: String = "",
        val confirmPassword: String = "",

        val loginError: String? = null,
        val passwordError: String? = null,
        val confirmPasswordError: String? = null,
        val isLoading: Boolean = false
    )

    sealed class RegisterEventState {
        data class ShowError(val message: String) : RegisterEventState()
        data class RedirectScreen(val screen: Screen) : RegisterEventState()
    }


    private val _registerUserInfo = MutableStateFlow(RegisterInfoState())
    val registerUserInfo = _registerUserInfo.asStateFlow()

    private val _registerEventSharedFlow = MutableSharedFlow<RegisterEventState>()
    val registerEventSharedFlow = _registerEventSharedFlow.asSharedFlow()


    fun onLoginChange(login: String) {
        _registerUserInfo.update {
            it.copy(
                login = login
            )
        }
    }

    fun onPasswordChange(password: String) {
        _registerUserInfo.update {
            it.copy(
                password = password
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _registerUserInfo.update {
            it.copy(
                confirmPassword = confirmPassword
            )
        }
    }


    fun registerUser() {

        if (!validateRegisterData())
            return

        viewModelScope.launch {

            _registerUserInfo.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                authRepository.registerUser(
                    AuthRequestDto(
                        login = registerUserInfo.value.login,
                        password = registerUserInfo.value.password,
                    )
                )
            }
            when (result) {

                is Resource.Success -> {
                    _registerUserInfo.update {
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
                            _registerEventSharedFlow.emit(
                                RegisterEventState.RedirectScreen(Screen.Home)
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    _registerUserInfo.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    val message = when (result.code) {
                        303 -> "Login déjà utilisé"
                        else -> "Erreur serveur"
                    }

                    _registerEventSharedFlow.emit(
                        RegisterEventState.ShowError(message)
                    )

                    if (result.code != 401)
                        Log.e("Login view Model", result.code.toString())
                }
            }
        }
    }


    private fun validateRegisterData(): Boolean {

        var isValid = false

        _registerUserInfo.let { registerData ->
            when {
                registerData.value.login.isBlank() ->
                    registerData.update { it.copy(loginError = "Login requis") }

                registerData.value.password.isBlank() ->
                    registerData.update { it.copy(passwordError = "Mot de passe requis") }

                !Regex(STRONG_PASSWORD).matches(registerData.value.password) ->
                    registerData.update {
                        it.copy(
                            passwordError =
                            "Mdp pas assez fort : 8 caractères, une majuscule, une minuscule, un chiffre et caractère (!#@))"
                        )
                    }

                registerData.value.password != registerData.value.confirmPassword ->
                    registerData.update { it.copy(confirmPasswordError = "confirmation mdp requise") }
                else -> isValid = true
            }
        }
        return isValid
    }
}

