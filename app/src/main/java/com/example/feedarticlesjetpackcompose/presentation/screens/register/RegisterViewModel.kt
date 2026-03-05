package com.example.feedarticlesjetpackcompose.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.data.dto.request.AuthRequestDto
import com.example.feedarticlesjetpackcompose.data.local.AuthSharedPref
import com.example.feedarticlesjetpackcompose.data.repository.AuthRepository
import com.example.feedarticlesjetpackcompose.utils.Resource
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.presentation.screens.common.FeedArticleEventState
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.feedarticlesjetpackcompose.utils.isConfirmPasswordValid
import com.example.feedarticlesjetpackcompose.utils.isStrongPassword
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

        val loginError: Int? = null,
        val passwordError: Int? = null,
        val confirmPasswordError: Int? = null,

        val isLoading: Boolean = false
    )


    private val _registerUserInfoStateFlow = MutableStateFlow(RegisterInfoState())
    val registerUserInfoStateFlow = _registerUserInfoStateFlow.asStateFlow()

    private val _registerEventSharedFlow = MutableSharedFlow<FeedArticleEventState>()
    val registerEventSharedFlow = _registerEventSharedFlow.asSharedFlow()


    fun onLoginChange(login: String) {
        _registerUserInfoStateFlow.update {
            it.copy(
                login = login
            )
        }
    }

    fun onPasswordChange(password: String) {
        _registerUserInfoStateFlow.update {
            it.copy(
                password = password
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _registerUserInfoStateFlow.update {
            it.copy(
                confirmPassword = confirmPassword
            )
        }
    }

    fun registerUser() {

        if (!validateRegisterData())
            return

        viewModelScope.launch {

            _registerUserInfoStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                authRepository.registerUser(
                    AuthRequestDto(
                        login = registerUserInfoStateFlow.value.login,
                        password = registerUserInfoStateFlow.value.password,
                    )
                )
            }
            when (result) {

                is Resource.Success -> {
                    _registerUserInfoStateFlow.update {
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
                                FeedArticleEventState.RedirectScreen(Screen.Home)
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    _registerUserInfoStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _registerEventSharedFlow.emit(
                        FeedArticleEventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }


    private fun validateRegisterData(): Boolean {

        val currentData = registerUserInfoStateFlow.value

        _registerUserInfoStateFlow.update {
            it.copy(
                loginError = when {
                    currentData.login.isEmpty() -> R.string.login_field_supporting_text_login_required
                    else -> null
                },
                passwordError = when {
                    currentData.password.isEmpty() -> R.string.password_field_supporting_text_password_required
                    !currentData.password.isStrongPassword() -> R.string.password_field_supporting_text_strong_password_required
                    else -> null
                },
                confirmPasswordError = when {
                    !currentData.confirmPassword.isConfirmPasswordValid(currentData.password) ->  R.string.confirm_password_field_supporting_text_login_required
                    else -> null
                }
            )
        }

        currentData.let {
            if (it.loginError != null || it.passwordError != null || it.confirmPasswordError != null)
                return false
        }
        return true
    }
}

