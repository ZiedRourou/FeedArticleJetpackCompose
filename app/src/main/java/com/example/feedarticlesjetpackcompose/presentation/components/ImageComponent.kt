package com.example.feedarticlesjetpackcompose.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.feedarticlesjetpackcompose.R

@Composable
fun FADisplayImageOrPlaceHolder(
    image : String,
    onError : ((String) ->Unit) = { },
    errorImage: Int,
    placeholder : Int,
    imageSize: Dp = 100.dp,
){
    if (image.isNotEmpty()) {
        AsyncImage(
            model = if (image.isEmpty()) null else ImageRequest
                .Builder(LocalContext.current)
                .data(image).crossfade(true).build(),
            placeholder = painterResource(placeholder),
            contentDescription = null,
            modifier = Modifier.size(imageSize).clip(CircleShape),
            contentScale = ContentScale.Crop,
            error = painterResource(errorImage),
            onError = { onError("") }
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.feedarticles_logo),
            contentDescription = null,
            Modifier.size(imageSize).clip(CircleShape),
            contentScale = ContentScale.Crop,
            )
    }
}