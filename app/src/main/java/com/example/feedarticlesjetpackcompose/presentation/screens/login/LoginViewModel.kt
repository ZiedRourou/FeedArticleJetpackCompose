package com.example.feedarticlesjetpackcompose.presentation.screens.login


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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authSharedPref: AuthSharedPref
) : ViewModel() {

    data class LoginInfoState(
        val login: String = "",
        val password: String = "",

        val loginError: Int? = null,
        val passwordError: Int? = null,

        val isLoading: Boolean = false
    )

    private val _loginUserInfoStateFlow = MutableStateFlow(LoginInfoState())
    val loginUserStateFlow = _loginUserInfoStateFlow.asStateFlow()

    private val _loginEventSharedFlow = MutableSharedFlow<FeedArticleEventState>()
    val loginEventSharedFlow = _loginEventSharedFlow.asSharedFlow()


    fun onLoginChange(login: String) {
        _loginUserInfoStateFlow.update {
            it.copy(
                login = login
            )
        }
    }

    fun onPasswordChange(password: String) {
        _loginUserInfoStateFlow.update {
            it.copy(
                password = password
            )
        }
    }

    fun onRegisterScreen() {
        viewModelScope.launch {
            _loginEventSharedFlow.emit(
                FeedArticleEventState.RedirectScreen(Screen.Register)
            )
        }
    }

    fun loginUser() {

        if (!validateLoginData())
            return

        viewModelScope.launch {

            _loginUserInfoStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                authRepository.loginUser(
                    AuthRequestDto(
                        login = loginUserStateFlow.value.login,
                        password = loginUserStateFlow.value.password,
                    )
                )
            }

            when (result) {
                is Resource.Success -> {
                    _loginUserInfoStateFlow.update {
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
                            FeedArticleEventState.RedirectScreen(Screen.Home)
                        )
                    }
                }

                is Resource.Error -> {
                    _loginUserInfoStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _loginEventSharedFlow.emit(
                        FeedArticleEventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }


    private fun validateLoginData(): Boolean {

        val currentData = loginUserStateFlow.value

        _loginUserInfoStateFlow.update {
            it.copy(
                loginError = when {
                    currentData.login.isEmpty() -> R.string.login_field_supporting_text_login_required
                    else -> null
                },
                passwordError = when {
                    currentData.password.isEmpty() -> R.string.password_field_supporting_text_password_required
                    else -> null
                }
            )
        }

        currentData.let {

            if (it.loginError != null || it.passwordError != null)
                return false

            if(!it.password.isStrongPassword()){
                viewModelScope.launch {
                    _loginEventSharedFlow.emit(
                        FeedArticleEventState.ShowMessageSnackBar(R.string.login_wrong_info_error)
                    )
                }
                return false
            }
        }
        return true
    }
}

