package com.example.feedarticlesjetpackcompose.data.repository


import com.example.feedarticlesjetpackcompose.data.api.ApiInterface
import com.example.feedarticlesjetpackcompose.data.dto.request.AuthRequestDto
import com.example.feedarticlesjetpackcompose.data.dto.response.AuthResponseDto

import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val api: ApiInterface,
) {

    suspend fun loginUser(
        loginData: AuthRequestDto
    ): Resource<AuthResponseDto> {

        val response = api.authLogin(loginData.login, loginData.password)

        if (response?.code() == 200 || response?.code() == 304) {
            response.body()?.let {
                return Resource.Success(
                    data = it,
                    code = response.code()
                )
            }
        }else
            return Resource.Error(response?.code()?: 400)

        return Resource.Error(400)
    }


    suspend fun registerUser(
        registerData: AuthRequestDto
    ): Resource<AuthResponseDto> {

        val response = api.authRegister(registerData)

        if (response?.code() == 200 ) {
            response.body()?.let {
                return Resource.Success(
                    data = it,
                    code = response.code()
                )
            }
        }else
            return Resource.Error(response?.code()?: 400)

        return Resource.Error(400)
    }
}
