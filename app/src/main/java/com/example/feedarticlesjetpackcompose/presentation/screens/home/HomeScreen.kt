package com.example.feedarticlesjetpackcompose.presentation.screens.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.feedarticlesjetpackcompose.R
import com.example.feedarticlesjetpackcompose.data.dto.response.ArticleResponseDto
import com.example.feedarticlesjetpackcompose.presentation.navigation.Screen
import com.example.feedarticlesjetpackcompose.ui.theme.FeedArticlesJetpackComposeTheme
import com.example.feedarticlesjetpackcompose.utils.Category
import com.example.feedarticlesjetpackcompose.utils.categoriesEditOrCreate
import com.example.feedarticlesjetpackcompose.utils.categoriesHomeFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    val articles by viewModel.homeStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect("single") {
        viewModel.homeEventSharedFlow.collect { event ->
            when (event) {

                is HomeViewModel.HomeEventState.ShowError ->
                    snackBarHostState.showSnackbar(event.message)

                is HomeViewModel.HomeEventState.RedirectScreen -> {
                    if (event.screen is Screen.Login) {
                        navController.navigate(event.screen.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    } else
                        navController.navigate(event.screen.route)
                }
                is HomeViewModel.HomeEventState.RedirectEditScreen -> {
                    navController.navigate(event.screen)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopBarHomeContent(
                onAddArticle = viewModel::navigateToAddArticle,
                onLogout = viewModel::logout
            )
        },
        content = { paddingVal ->
            HomeContent(
                paddingVal,
                articles,
                onDelete = viewModel::onDeleteArticle,
                isAuthor = viewModel::isAuthor,
                onClickItem = viewModel::onClickItem
            )
        },
        bottomBar = {
            BottomBarHome(
                listOptions = articles.categoriesOptions,
                selectedOption = articles.selectedCategory,
                onSelectCategory = viewModel::onSelectCategory
            )
        }
    )
}

@Composable
private fun TopBarHomeContent(
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
        IconButton(onClick = { onAddArticle() }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        IconButton(onClick = { onLogout() }) {
            Icon(
                imageVector = Icons.Filled.PowerSettingsNew,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun BottomBarHome(
    listOptions: Array<Category>,
    selectedOption: Category,
    onSelectCategory: (Category) -> Unit

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.Center
    ) {

        listOptions.forEach { option ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    RadioButton(
                        selected = selectedOption.id == option.id,
                        onClick = { onSelectCategory(option) }
                    )
                    Text(option.name, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun HomeContent(
    paddingValues: PaddingValues,
    articles: HomeViewModel.HomeState,
    onDelete: (Int) -> Unit,
    isAuthor: (Int) -> Boolean,
    onClickItem: (Boolean,ArticleResponseDto) -> Boolean
) {

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        if (articles.filteredArticles != null)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = articles.filteredArticles,
                    key = { article -> article.id }
                )
                { item ->
                    SwipeBox(
                        onDelete = { onDelete(item.id) },
                        enableSwipe = isAuthor(item.idUserAuthor),
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBox(
    onDelete: () -> Unit,
    content: @Composable () -> Unit,
    enableSwipe: Boolean
) {
    val swipeState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        modifier = Modifier.padding(horizontal = 10.dp),
        state = swipeState,
        backgroundContent = {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxSize()
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

    if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart)
        onDelete()
}


@Composable
private fun ItemArticle(
    article: ArticleResponseDto,
    onClick : (Boolean,ArticleResponseDto) -> Boolean
) {
    var expandedState by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            categoriesEditOrCreate.firstOrNull { it.id == article.categoryId }?.color
                ?: Color.LightGray
        ),

        onClick = {
            expandedState = onClick(expandedState,article)
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            AsyncImage(
                model = if (article.urlImage.isEmpty()) null else ImageRequest
                    .Builder(LocalContext.current)
                    .data(article.urlImage).crossfade(true).build(),
                placeholder = painterResource(id = R.drawable.feedarticles_logo),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                error = painterResource(id = R.drawable.feedarticles_logo),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
            )
        }

        if (expandedState) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
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
                        Text(text = article.createdAt)
                        Text(text = Category.Sport.name)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = article.content,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }

}

@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun PreviewHome() {
    val options = listOf("Tout", "Sport", "Manga", "Divers")
    val selectedOption = remember { mutableStateOf(options[0]) }

    FeedArticlesJetpackComposeTheme {
        Scaffold(
            topBar = {
                TopBarHomeContent(
                    onAddArticle = {},
                    onLogout = {}
                )
            },
            content = { paddingVal ->
                HomeContent(
                    paddingVal,
                    articles = HomeViewModel.HomeState(
                        filteredArticles =
                        listOf(
                            ArticleResponseDto(
                                0,
                                "Justin gateteghe a un nouvel adversaire pour l'ufc 313 avec rafael fiziev",
                                "fffqsdfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffsf",
                                "",
                                2,
                                "du 06/02/2025",
                                0
                            ),
                            ArticleResponseDto(
                                1,
                                "Justin gateteghe a un nouvel adversaire pour l'ufc 313 avec rafael fiziev",
                                "fffqsdfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffsf",
                                "",
                                2,
                                "du 06/02/2025",
                                0
                            )
                        )
                    ),
                    onDelete = {},
                    isAuthor = {true},
                    onClickItem = { x,y -> false}
                )
            },
            bottomBar = {
                BottomBarHome(
                    listOptions = categoriesHomeFilter,
                    selectedOption = Category.Diverse,
                    onSelectCategory = {}
                )
            }
        )
    }
}


