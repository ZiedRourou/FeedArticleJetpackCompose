package com.example.feedarticlesjetpackcompose.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedarticlesjetpackcompose.data.dto.response.ArticleResponseDto
import com.example.feedarticlesjetpackcompose.presentation.components.FARadioGroupButtonCategories
import com.example.feedarticlesjetpackcompose.presentation.components.ItemArticle
import com.example.feedarticlesjetpackcompose.presentation.components.LoadingOverlay
import com.example.feedarticlesjetpackcompose.presentation.components.SwipeBox
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme
import com.example.feedarticlesjetpackcompose.utils.Category
import com.example.feedarticlesjetpackcompose.utils.categoriesHomeFilter


@Composable
fun TopBarHomeContent(
    onAddArticle: () -> Unit,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onAddArticle) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        IconButton(onClick = onLogout) {
            Icon(
                imageVector = Icons.Filled.PowerSettingsNew,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun BottomBarHome(
    listOptions: ArrayList<Category>,
    selectedOption: Category,
    onSelectCategory: (Category) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {

        FARadioGroupButtonCategories(
            listOptions = listOptions,
            selectedOption = selectedOption,
            onSelectChange = onSelectCategory
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    articles: HomeViewModel.HomeState,
    onDelete: (Int) -> Unit,
    isAuthor: (Int) -> Boolean,
    onClickItem: (Boolean, ArticleResponseDto) -> Boolean,
) {

    LoadingOverlay(isVisible = articles.isLoading)

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        if (articles.filteredArticles != null)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = LazyListState(firstVisibleItemIndex = 0)
            ) {
                items(
                    items = articles.filteredArticles,
                    key = { article -> article.id }
                )
                { item ->
                    SwipeBox(
                        onDelete = { onDelete(item.id) },
                        enableSwipeVM = isAuthor(item.idUserAuthor),
                        content = {
                            ItemArticle(
                                article = item,
                                onClick = onClickItem
                            )
                        }
                    )
                }
            }
    }
}


@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun PreviewHome() {

//    FeedArticlesJetpackComposeTheme {
//        Scaffold(
//            topBar = {
//                TopBarHomeContent(
//                    onAddArticle = {},
//                    onLogout = {}
//                )
//            },
//            content = { paddingVal ->
//                HomeContent(
//                    paddingVal,
//                    articles = HomeViewModel.HomeState(
//                        filteredArticles =
//                        listOf(
//                            ArticleResponseDto(
//                                0,
//                                "Justin gataiiev",
//                                "fffqsdfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffsf",
//                                "",
//                                2,
//                                "du 06/02/2025",
//                                0
//                            ),
//                            ArticleResponseDto(
//                                1,
//                                "Justin gateteghe a un nouvel adversaire pour l'ufc 313 avec rafael fiziev",
//                                "fffqsdfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffsf",
//                                "",
//                                2,
//                                "du 06/02/2025",
//                                0
//                            )
//                        )
//                    ),
//                    onDelete = {},
//                    isAuthor = { true },
//                    onClickItem = { x, y -> false }
//                )
//            },
//            bottomBar = {
//                BottomBarHome(
//                    listOptions = categoriesHomeFilter,
//                    selectedOption = Category.Diverse,
//                    onSelectCategory = {}
//                )
//            }
//        )
//    }
}
