package com.example.feedarticlesjetpackcompose.data.repository


import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.data.api.ApiInterface
import com.example.feedarticlesjetpackcompose.data.dto.request.AuthRequestDto
import com.example.feedarticlesjetpackcompose.data.dto.response.AuthResponseDto
import com.example.feedarticlesjetpackcompose.utils.Resource
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val api: ApiInterface,
) {

    suspend fun loginUser(
        loginData: AuthRequestDto
    ): Resource<AuthResponseDto> {

        val response = api.authLogin(loginData.login, loginData.password)

        if (response?.code() == 200 || response?.code() == 304) {
            response.body()?.let { userCredential ->
                return Resource.Success(
                    data = userCredential,
                    code = response.code()
                )
            }
        } else
            return Resource.Error(
                code = response?.code() ?: 400,
                message = when (response?.code()) {
                    401 -> R.string.login_wrong_info_error
                    else -> R.string.error_server
                }
            )
        return Resource.Error(400, R.string.error_server)
    }


    suspend fun registerUser(
        registerData: AuthRequestDto
    ): Resource<AuthResponseDto> {

        val response = api.authRegister(registerData)

        if (response?.code() == 200) {
            response.body()?.let { userCredential ->
                return Resource.Success(
                    data = userCredential,
                    code = response.code()
                )
            }
        } else
            return Resource.Error(
                code = response?.code() ?: 400,
                message = when (response?.code()) {
                    303 -> R.string.error_login_already_used
                    else -> R.string.error_server
                }
            )
        return Resource.Error(400, R.string.error_server)
    }
}
