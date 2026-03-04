package com.example.feedarticlesjetpackcompose.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.feedarticlesjetpackcompose.R


const val KEY_FILENAME = "sharePreferencesAuth"
const val KEY_IS_LOGIN = "isLogin"
const val KEY_TOKEN = "token"
const val KEY_USERID = "userId"

const val STRONG_PASSWORD_REGEX =
    "^(?=(.*[a-z]){3,})(?=(.*[A-Z]){1,})(?=(.*[0-9]){1,})(?=(.*[!@#]){1,}).{8,}"


sealed class Category(
    val name: Int,
    val id: Int,
    val color: Color
) {
    data object Anime : Category(R.string.category_label_anime, 1, Color.Red)
    data object Sport : Category(R.string.category_abel_sport, 2, Color.LightGray)
    data object Diverse : Category(R.string.category_abel_diverse, 3, Color.Yellow)
    data object All : Category(R.string.category_abel_all, 0, Color.Blue)
}

val categoriesEditOrCreate  = arrayListOf(Category.Anime,Category.Sport,Category.Diverse)
val categoriesHomeFilter  = arrayListOf(Category.All,Category.Anime,Category.Sport,Category.Diverse)