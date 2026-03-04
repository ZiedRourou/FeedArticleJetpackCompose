package com.example.feedarticlesjetpackcompose.utils

import androidx.compose.ui.graphics.Color


const val KEY_FILENAME = "sharePreferencesAuth"
const val KEY_IS_LOGIN = "isLogin"
const val KEY_TOKEN = "token"
const val KEY_USERID = "userId"

const val AUTH_TOKEN_NOT_UPDATE = 5
const val AUTH_OK = 1
const val AUTH_NOT_FOUND = 0
const val AUTH_ERROR_PARAMETER = -1

const val AUTH_REGISTER_ALREADY_EXIST = 5

const val FETCH_ARTICLE_OK = "ok"
const val POST_ARTICLE_OK = 1
const val UNAUTHORIZED = "unauthorized"

const val STRONG_PASSWORD =
    "^(?=(.*[a-z]){3,})(?=(.*[A-Z]){1,})(?=(.*[0-9]){1,})(?=(.*[!@#]){1,}).{8,}"


sealed class Category(
    val name: String,
    val id: Int,
    val color: Color
) {
    data object Anime : Category("Manga", 1, Color.Red)
    data object Sport : Category("Sport", 2, Color.LightGray)
    data object Diverse : Category("Divers", 3, Color.Yellow)
    data object All : Category("Tous", 0, Color.Blue)
}

val categoriesEditOrCreate  = arrayOf(Category.Anime,Category.Sport,Category.Diverse)
val categoriesHomeFilter  = arrayOf(Category.All,Category.Anime,Category.Sport,Category.Diverse)