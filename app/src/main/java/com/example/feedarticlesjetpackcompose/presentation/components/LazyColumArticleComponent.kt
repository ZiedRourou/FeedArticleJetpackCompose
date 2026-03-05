package com.example.feedarticlesjetpackcompose.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.data.dto.response.ArticleResponseDto
import com.example.feedarticlesjetpackcompose.utils.Category
import com.example.feedarticlesjetpackcompose.utils.categoriesEditOrCreate
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBox(
    onDelete: () -> Unit,
    content: @Composable () -> Unit,
    enableSwipeVM: Boolean
) {
    var enableSwipe by remember { mutableStateOf(enableSwipeVM) }

    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if (state == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                enableSwipe= false
            }
            true
        }
    )

    SwipeToDismissBox(
        modifier = Modifier.padding(horizontal = 10.dp),
        state = swipeState,
        backgroundContent = {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .background(MaterialTheme.colorScheme.errorContainer)
            ) {
                Icon(
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    imageVector = Icons.Outlined.Delete, contentDescription = null
                )
            }
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = enableSwipe
    ) {
        content()
    }
}


@Composable
fun ItemArticle(
    article: ArticleResponseDto,
    onClick: (Boolean, ArticleResponseDto) -> Boolean
) {
    var expandedState by remember { mutableStateOf(false) }

    val animatedSize by animateDpAsState(
        targetValue = if(expandedState) 80.dp else 50.dp,
        animationSpec = tween(500, easing = LinearOutSlowInEasing),
        label = ""
    )

    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            categoriesEditOrCreate.firstOrNull { it.id == article.categoryId }?.color
                ?: Color.LightGray
        ),
        onClick = {
            expandedState = onClick(expandedState, article)
        },

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            FADisplayImageOrPlaceHolder(
                image = article.urlImage,
                errorImage = R.drawable.feedarticles_logo,
                placeholder = R.drawable.feedarticles_logo,
                imageSize = animatedSize
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
            )
        }

        AnimatedVisibility(
            visible = expandedState,
            enter = fadeIn() +  expandVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            ArticleDetail(
                date = article.createdAt,
                content = article.content,
                category = categoriesEditOrCreate.first { it.id == article.categoryId })
        }
    }
}

@Composable
fun ArticleDetail(
    date: String,
    content: String,
    category: Category
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp).
            animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = date)
                Text(text = stringResource(category.name))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}