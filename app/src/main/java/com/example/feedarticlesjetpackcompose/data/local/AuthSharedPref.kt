package com.example.feedarticlesjetpackcompose.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.feedarticlesjetpackcompose.utils.KEY_FILENAME
import com.example.feedarticlesjetpackcompose.utils.KEY_IS_LOGIN
import com.example.feedarticlesjetpackcompose.utils.KEY_TOKEN
import com.example.feedarticlesjetpackcompose.utils.KEY_USERID

class AuthSharedPref(context: Context) {

    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(KEY_FILENAME, Context.MODE_PRIVATE)

    fun isLogin(): Boolean = sharedPreference.getBoolean(KEY_IS_LOGIN, false)

    fun saveUserInfo(
        token: String, userId: Int
    ) {
        sharedPreference.edit()
            .putBoolean(KEY_IS_LOGIN, true)
            .putString(KEY_TOKEN, token)
            .putInt(KEY_USERID, userId)
            .apply()
    }

    fun getToken(): String = sharedPreference.getString(KEY_TOKEN, "")!!

    fun getUserId() = sharedPreference.getInt(KEY_USERID, 0)

    fun clearLogin() {
        sharedPreference.edit().clear().apply()
    }

}