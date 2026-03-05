package com.example.feedarticlesjetpackcompose.utils

sealed class Resource<T>(
    val code: Int,
    val data: T? = null,
    val message : Int = 0
) {
    class Success<T>(data: T?, code: Int) : Resource<T>(code = code, data = data)
    class Error<T>(code: Int, message: Int) : Resource<T>(code = code,message= message)
}
