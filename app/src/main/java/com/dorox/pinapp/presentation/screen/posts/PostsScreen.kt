package com.dorox.pinapp.presentation.screen.posts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.dorox.pinapp.R
import com.dorox.pinapp.domain.entity.Category
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.entity.Status
import com.dorox.pinapp.presentation.screen.post_details.PostDetailsSheet
import com.dorox.pinapp.presentation.screen.post_details.PostDetailsViewModel
import com.dorox.pinapp.presentation.screen.select_city.SelectCitySheet
import com.dorox.pinapp.presentation.screen.select_city.SelectCityViewModel
import com.dorox.pinapp.presentation.utils.toShortDateString
import com.dorox.pinapp.ui.theme.Black100
import com.dorox.pinapp.ui.theme.CustomOutlinedTextField
import com.dorox.pinapp.ui.theme.ErrorButton
import com.dorox.pinapp.ui.theme.Gray100
import com.dorox.pinapp.ui.theme.Gray150
import com.dorox.pinapp.ui.theme.Gray50
import com.dorox.pinapp.ui.theme.Gray500
import com.dorox.pinapp.ui.theme.Gray550
import com.dorox.pinapp.ui.theme.Green100
import com.dorox.pinapp.ui.theme.Green200
import com.dorox.pinapp.ui.theme.LoadingIndicator
import com.dorox.pinapp.ui.theme.Orange10
import com.dorox.pinapp.ui.theme.Orange100
import com.dorox.pinapp.ui.theme.Orange200
import com.dorox.pinapp.ui.theme.TextBoxWithStartIcon
import com.dorox.pinapp.ui.theme.Orange300
import com.dorox.pinapp.ui.theme.Orange400
import com.dorox.pinapp.ui.theme.Orange5
import com.dorox.pinapp.ui.theme.Orange50
import com.dorox.pinapp.ui.theme.Red100
import com.dorox.pinapp.ui.theme.Red200
import com.dorox.pinapp.ui.theme.SelectCategoryFilterChips
import com.dorox.pinapp.ui.theme.White100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(
    postsViewModel: PostsViewModel = hiltViewModel(),
    onCreatePostClick: () -> Unit,
    postDetailsViewModel: PostDetailsViewModel = hiltViewModel(),
    selectCityViewModel: SelectCityViewModel = hiltViewModel()
) {

    val state by postsViewModel.state.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheetPostDetails by remember { mutableStateOf(false) }
    var showBottomSheetSelectCity by remember { mutableStateOf(false) }

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by postsViewModel.isRefreshing.collectAsState()

    //val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val categories = Category.entries.filter {
        it != Category.NOT_SELECTED
    }.toList()

    var selectedChipCategory by remember { mutableStateOf(Category.ALL) }

    Scaffold(
        modifier = Modifier,
        topBar = {
            PostsScreenTopAppBar(
                onRefreshClick = {
                    postsViewModel.refreshPosts()
                },
//                onSelectCategoryClick = {
//                    viewModel.processCommand(PostsCommand.SelectCategory(category = it))
//                },
                //scrollBehavior = scrollBehavior
                cityName = state.selectedCity,
                onSelectCityClick = {
                    showBottomSheetSelectCity = true
                },
                onClearCityClick = {
                    postsViewModel.processCommand(PostsCommand.ClearCity)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onCreatePostClick()
                },
                containerColor = White100,
                contentColor = Orange300,
                shape = RoundedCornerShape(24.dp),
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
                .background(color = Orange5),
            isRefreshing = isRefreshing,
            onRefresh = {
                postsViewModel.refreshPosts()
            },
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    color = Orange300,
                    containerColor = White100
                )
            }
        ) {

            Column(

            ) {
                if (state.isLoading && state.posts.isEmpty()) {
                    LoadingIndicator()

                }
                if (state.error != null && state.posts.isEmpty()) {
                    ErrorButton(
                        errorText = state.error.toString(),
                        onRefreshButtonClick = {
                            postsViewModel.refreshPosts()
                        }
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    //verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    item {
                        CustomOutlinedTextField(
                            value = state.query,
                            label = {
                                Text("Поиск объявления")
                            },
                            onValueChange = { query ->
                                postsViewModel.processCommand(PostsCommand.InputSearchQuery(query))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                                    contentDescription = null,
                                    tint = Orange300
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        postsViewModel.processCommand(PostsCommand.ClearQuery)
                                    }
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
                                        contentDescription = null,
                                        tint = Orange300
                                    )
                                }
                            }
                        )
                    }

                    item {
                        SelectCategoryFilterChips(
                            modifier = Modifier,
                            onSelectCategoryClick = {
                                selectedChipCategory = it
                                postsViewModel.processCommand(PostsCommand.SelectCategory(category = it))
                            },
                            categories = categories,
                            selectedChipCategory = selectedChipCategory,
                        )
                    }



                    items(
                        items = state.filteredPosts,
                        key = { it.postId }
                    ) { post ->
                        PostCard(
                            modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                            onPostClick = { postId ->
                                //selectedPostId = postId
                                postDetailsViewModel.loadPostDetails(postId)
                                showBottomSheetPostDetails = true
                            },
                            post = post,
                        )
                    }
                }
            }

            if (showBottomSheetPostDetails) {
                ModalBottomSheet(
                    onDismissRequest = {
                        //selectedPostId = null
                        postDetailsViewModel.clearPostDetails()
                        showBottomSheetPostDetails = false
                    },
                    containerColor = White100,
                    sheetState = bottomSheetState,
                    contentWindowInsets = { WindowInsets.systemBars.only(WindowInsetsSides.Bottom) }
                ) {
                    PostDetailsSheet(
                        viewModel = postDetailsViewModel
                    )
                }
            }
            if (showBottomSheetSelectCity) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheetSelectCity = false
                    },
                    containerColor = Orange5,
                    sheetState = bottomSheetState,
                    //contentWindowInsets = { WindowInsets.systemBars.only(WindowInsetsSides.Bottom) }
                ) {
                    SelectCitySheet(
                        viewModel = selectCityViewModel,
                        onCitySelect = { city ->
                            postsViewModel.processCommand(PostsCommand.SelectCity(city = city))
                            showBottomSheetSelectCity = false
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostsScreenTopAppBar(
    onRefreshClick: () -> Unit,
    cityName: String? = null,
    onSelectCityClick: () -> Unit,
    //scrollBehavior: TopAppBarScrollBehavior
    onClearCityClick: () -> Unit
) {

    Column {
        TopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val containerColor = if (cityName == null) White100 else Orange300.copy(0.1f)
                    val contentColor = if (cityName == null) Gray500 else Orange300
                    val borderColor = if (cityName == null) Gray50 else Orange300

                    Button(
                        modifier = Modifier,
                        onClick = {
                            onSelectCityClick()
                        },
                        shape =  RoundedCornerShape(24.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = containerColor,
                            contentColor = contentColor
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = borderColor
                        )
                    ) {
                        val text = if (cityName == null)
                            "Выбрать город"
                        else
                        "г. ${cityName.substringBefore(",")}"

                        Text(
                            modifier = Modifier,
                            text = text,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        if (cityName != null) {

//                            IconButton(
//                                modifier = Modifier,
//                                onClick = {
//                                    onClearCityClick()
//                                }
//                            ) {
//                                Icon(
//                                    modifier = Modifier.size(20.dp),
//                                    imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
//                                    contentDescription = null,
//                                    tint = Black100
//                                )
//                            }
                        }
                    }
                    if (cityName != null) {

                        TextButton(
                            onClick = {
                                onClearCityClick()
                            }
                        ) {
                            Text(
                                text = "Сбросить"
                            )
                        }
                    }

                }

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
            //scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Orange5
            )
        )



//        AnimatedVisibility(
//            visible =
//        ) {
//            SelectCategoryFilterChips(
//                onSelectCategoryClick = {
//                    onSelectCategoryClick(it)
//                    selectedChipCategory = it
//                },
//                categories = categories,
//                selectedChipCategory = selectedChipCategory,
//            )
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
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                onPostClick(post.postId)
            },
        elevation = CardDefaults.cardElevation(0.5.dp),
        colors = CardDefaults.cardColors(
            containerColor = White100
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
                    horizontalArrangement = Arrangement.Start
                ) {
                    val statusBackgroundColor =
                        if (post.status == Status.FOUND.text) Green100
                        else Red100

                    val statusTextColor =
                        if (post.status == Status.FOUND.text) Green200
                        else Red200

                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(color = statusTextColor)
                            .padding(0.dp)
                            .align(Alignment.CenterVertically)
                    )

                    Spacer(Modifier.width(4.dp))

                    TextBoxWithStartIcon(
                        text = post.status,
                        colorText = statusTextColor,
                        colorBackground = Color.Transparent,
                        horizontalDp = 0.dp,
                    )

                    TextBoxWithStartIcon(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        text = post.createdAt.toShortDateString(),
                        colorText = Gray500,
                        colorBackground = Color.Transparent,
                        horizontalDp = 0.dp,
                        contentAlignment = Alignment.TopEnd
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

//                Spacer(Modifier.height(4.dp))

//                Text(
//                    text = post.description,
//                    style = MaterialTheme.typography.bodyMedium,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                )

                Spacer(Modifier.height(8.dp))

                TextBoxWithStartIcon(
                    text = post.address,
                    colorText = Black100,
                    colorBackground = Color.Transparent,
                    horizontalDp = 0.dp,
                    icon = ImageVector.vectorResource(R.drawable.ic_location_pin),
                    tint = Black100
                )

                Spacer(Modifier.height(8.dp))

                if (post.reward.isNotEmpty()) {
                    TextBoxWithStartIcon(
                        text = stringResource(R.string.reward, post.reward),
                        colorText = Orange300,
                        fontWeightText = FontWeight.Bold,
                        colorBackground = Orange5,
                        horizontalDp = 8.dp,
                        verticalDp = 4.dp,
                        icon = ImageVector.vectorResource(R.drawable.ic_money_bag),
                        tint = Orange300
                    )
                }
            }

            if (post.imageUrl != "") {

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(75.dp, 75.dp, 75.dp, 75.dp)
                        .padding(start = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            modifier = Modifier,
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
}

@Composable
private fun SearchOutlinedTextField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp),
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Поиск по объявлениям",
                color = Gray100,
            )
        },
        shape = RoundedCornerShape(24.dp),
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
                    onQueryChange("")
                }
            ) {
                Icon(
                    modifier = Modifier,
                    imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
                    tint = Gray100,
                    contentDescription = null
                )
            }

        }
    )
}