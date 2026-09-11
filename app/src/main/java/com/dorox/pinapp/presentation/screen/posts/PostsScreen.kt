package com.dorox.pinapp.presentation.screen.posts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.dorox.pinapp.R
import com.dorox.pinapp.domain.entity.Category
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.entity.Status
import com.dorox.pinapp.presentation.screen.postdetails.PostDetailsSheet
import com.dorox.pinapp.presentation.screen.postdetails.PostDetailsViewModel
import com.dorox.pinapp.presentation.utils.toShortDateString
import com.dorox.pinapp.ui.theme.CustomTextButton
import com.dorox.pinapp.ui.theme.Gray100
import com.dorox.pinapp.ui.theme.Gray150
import com.dorox.pinapp.ui.theme.Gray400
import com.dorox.pinapp.ui.theme.Gray50
import com.dorox.pinapp.ui.theme.Gray500
import com.dorox.pinapp.ui.theme.Green100
import com.dorox.pinapp.ui.theme.Green200
import com.dorox.pinapp.ui.theme.InfoTextBox
import com.dorox.pinapp.ui.theme.Orange10
import com.dorox.pinapp.ui.theme.Orange200
import com.dorox.pinapp.ui.theme.Orange50
import com.dorox.pinapp.ui.theme.Red100
import com.dorox.pinapp.ui.theme.Red200
import com.dorox.pinapp.ui.theme.Red300
import com.dorox.pinapp.ui.theme.SelectCategoryFilterChips
import com.dorox.pinapp.ui.theme.White100
import com.dorox.pinapp.ui.theme.Yellow50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(
    viewModel: PostsViewModel = hiltViewModel(),
    onCreatePostClick: () -> Unit,
    postDetailsViewModel: PostDetailsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PostsScreenTopAppBar(
                onRefreshClick = {
                    viewModel.refreshPosts()
                },
                onSelectCategoryClick = {
                    viewModel.processCommand(PostsCommand.SelectCategory(category = it))
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onCreatePostClick()
                },
                contentColor = Orange200,
                containerColor = Gray150,
                shape = CircleShape,

                ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_add_ad),
                    contentDescription = null
                )
            }
        },

    ) { innerPadding ->

        PullToRefreshBox(
            modifier = Modifier
                .padding(innerPadding)
                .background(color = Gray150),
            isRefreshing = isRefreshing,
            onRefresh = {
                viewModel.refreshPosts()
            },
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    color = Orange200,
                    containerColor = Gray150
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                item {
                    Spacer(Modifier.height(4.dp))
                }

                items(
                    items = state.filteredPosts,
                    key = { it.postId }
                ) {post ->
                    PostCard(
                        onPostClick = { postId ->
                            //selectedPostId = postId
                            postDetailsViewModel.loadPostDetails(postId)
                            showBottomSheet = true
                        },
                        post = post,
                    )
                }

                if (state.isLoading && state.posts.isEmpty()) {
                    item {
                        LoadingBox()
                    }
                }
                if (state.error != null && state.posts.isEmpty()) {
                    item {
                        ErrorBox(
                            state = state,
                            viewModel = viewModel
                        )
                    }
                }
                item {
                    Spacer(Modifier.height(4.dp))
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier,
                onDismissRequest = {
                    //selectedPostId = null
                    postDetailsViewModel.clearPostDetails()
                    showBottomSheet = false
                },
                containerColor = MaterialTheme.colorScheme.background,
                sheetState = sheetState
            ) {
                PostDetailsSheet(
                    viewModel = postDetailsViewModel
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostsScreenTopAppBar(
    onRefreshClick: () -> Unit,
    onSelectCategoryClick: (Category) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {

    val categories = Category.entries.filter {
        it != Category.NOT_SELECTED
    }.toList()

    var selectedChipCategory by remember { mutableStateOf(Category.ALL) }

    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Объявления",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            actions = {
                IconButton(
                    onClick =  onRefreshClick
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_refresh),
                        contentDescription = null,
                        tint = Gray500
                    )
                }
            },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = White100,
                scrolledContainerColor = White100
            )
        )
//        AnimatedVisibility(
//            visible =
//        ) {
            SelectCategoryFilterChips(
                onSelectCategoryClick = {
                    onSelectCategoryClick(it)
                    selectedChipCategory = it
                },
                categories = categories,
                selectedChipCategory = selectedChipCategory,
            )
//        }


    }

}

@Composable
private fun PostCard(
    modifier: Modifier = Modifier,
    onPostClick: (String) -> Unit,
    post: Post
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onPostClick(post.postId)
            },
        elevation = CardDefaults.cardElevation(0.5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(3f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val statusBackgroundColor = if (post.status == Status.FOUND.text) Green100 else Red100
                    val statusTextColor = if (post.status == Status.FOUND.text) Green200 else Red200

                    InfoTextBox(
                        text = post.status,
                        colorText = statusTextColor,
                        colorBackground = statusBackgroundColor,
                    )

                    InfoTextBox(
                        text = post.createdAt.toShortDateString(),
                        colorText = Gray100,
                        colorBackground = Color.Transparent,
                        horizontalDp = 0.dp,
                        contentAlignment = Alignment.TopEnd
                    )
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )

//                Spacer(Modifier.height(4.dp))

//                Text(
//                    text = post.description,
//                    style = MaterialTheme.typography.bodyMedium,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                )

                Spacer(Modifier.height(4.dp))

                InfoTextBox(
                    text = post.address,
                    colorText = Gray500,
                    colorBackground = Color.Transparent,
                    horizontalDp = 0.dp,
                    icon = ImageVector.vectorResource(R.drawable.ic_location_pin),
                    tint = Gray500
                )

                Spacer(Modifier.height(4.dp))

                if (post.reward.isNotEmpty()) {
                    InfoTextBox(
                        text = "${post.reward} ₽",
                        colorText = Orange200,
                        colorBackground = Orange10,
                        icon = ImageVector.vectorResource(R.drawable.ic_money_bag),
                        tint = Orange200
                    )
                }
            }

            if (post.imageUrl != "") {

                Spacer(Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f),
//                        .background(color = Gray500),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .sizeIn(
                                maxHeight = 100.dp,
                                maxWidth = 100.dp,
                                minWidth = 100.dp,
                                minHeight = 100.dp
                            )
                            .clip(RoundedCornerShape(16.dp)),
                        model = post.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_image_50)
                    )

                }
            }
        }
    }
}

@Composable
private fun SearchOutlinedTextField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Поиск по объявлениям",
                color = Gray100,
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Gray100,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White

        ),
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                tint = Gray100,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {

                }
            ) { }
            Icon(
                modifier = Modifier,
                imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
                tint = Gray100,
                contentDescription = null
            )
        }
    )
}

@Composable
private fun ErrorBox(
    state: PostsState,
    viewModel: PostsViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Text("${state.error}")

            Spacer(Modifier.height(8.dp))

            CustomTextButton(
                modifier = Modifier.padding(horizontal = 50.dp),
                onClick = {
                    viewModel.refreshPosts()
                }
            ) {
                Text(
                    text = "Повторить"
                )
            }
        }

    }
}

@Composable
private fun LoadingBox() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}