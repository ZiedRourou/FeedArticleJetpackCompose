package com.example.feedarticlesjetpackcompose.utils

import androidx.compose.ui.graphics.Color
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.ui.theme.AnimeColor
import com.example.feedarticlesjetpackcompose.ui.theme.DiversColor
import com.example.feedarticlesjetpackcompose.ui.theme.OtherColor
import com.example.feedarticlesjetpackcompose.ui.theme.SportColor


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
    data object Anime : Category(R.string.category_label_anime, 1, AnimeColor)
    data object Sport : Category(R.string.category_abel_sport, 2, SportColor)
    data object Diverse : Category(R.string.category_abel_diverse, 3, DiversColor)
    data object All : Category(R.string.category_abel_all, 0, OtherColor)
}

val categoriesEditOrCreate  = arrayListOf(Category.Anime,Category.Sport,Category.Diverse)
val categoriesHomeFilter  = arrayListOf(Category.All,Category.Anime,Category.Sport,Category.Diverse)