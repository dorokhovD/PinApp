package com.dorox.pinapp.presentation.screen.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dorox.pinapp.R
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.entity.Status
import com.dorox.pinapp.presentation.screen.postdetails.PostDetailsSheet
import com.dorox.pinapp.presentation.screen.postdetails.PostDetailsViewModel
import com.dorox.pinapp.presentation.utils.toDateString
import com.dorox.pinapp.ui.theme.CustomTextButton
import com.dorox.pinapp.ui.theme.Gray100
import com.dorox.pinapp.ui.theme.Gray400
import com.dorox.pinapp.ui.theme.Gray500
import com.dorox.pinapp.ui.theme.Green100
import com.dorox.pinapp.ui.theme.Green200
import com.dorox.pinapp.ui.theme.InfoBoxText
import com.dorox.pinapp.ui.theme.Orange100
import com.dorox.pinapp.ui.theme.Orange200
import com.dorox.pinapp.ui.theme.Red100
import com.dorox.pinapp.ui.theme.Red200
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(
    innerPadding: PaddingValues,
    viewModel: PostsViewModel = hiltViewModel(),
    postDetailsViewModel: PostDetailsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refreshPosts()

        },
        state = rememberPullToRefreshState()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Gray400)
                .padding(horizontal = 16.dp),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row {
                    SearchOutlinedTextField(
                        query = state.query,
                        onQueryChange = {
                            viewModel.processCommand(PostsCommand.InputSearchQuery(it))
                        }
                    )
                }
            }

            items(
                items = state.posts,
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
        ) {
            PostDetailsSheet(
                viewModel = postDetailsViewModel
            )
        }
    }
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
                },
                text = "Повторить"
            )
        }

    }
}

@Composable
private fun LoadingBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val statusBackgroundColor = if (post.status == Status.FOUND.text) Green100 else Red100
                val statusTextColor = if (post.status == Status.FOUND.text) Green200 else Red200

                InfoBoxText(
                    text = post.createdAt.toDateString(),
                    colorText = Gray100,
                    colorBackground = Color.Transparent,
                    horizontalDp = 0.dp,
                    contentAlignment = Alignment.TopEnd
                )

                InfoBoxText(
                    text = post.status,
                    colorText = statusTextColor,
                    colorBackground = statusBackgroundColor,
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = post.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,

            )

            Spacer(Modifier.height(8.dp))

//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//            ) {

                InfoBoxText(
                    text = post.address,
                    colorText = Gray500,
                    colorBackground = Color.Transparent,
                    horizontalDp = 0.dp,
                )

            Spacer(Modifier.height(8.dp))

                if (post.reward.isNotEmpty()) {
                    InfoBoxText(
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = Orange200,
                            shape = RoundedCornerShape(16.dp)
                            ),
                        text = "${post.reward} ₽",
                        colorText = Gray500,
                        colorBackground = Orange100,
                        contentAlignment = Alignment.TopEnd
                    )
                }
//            }
        }
    }
}

@Preview
@Composable
fun PreviewPostDetailsSheet(
) {
    Column {
        val s = "123"
        PostCard(
            onPostClick = {_ -> },
            post = Post(
                postId = "",
                userId = "",
                title = "Связка ключей с красным брелоком",
                description = "Потерял в районе Арбата в понедельник, " +
                                "в связке было четыре ключа, если нашли " +
                                    "просьба написать в тг @tgUserName",
                status = Status.LOST.text,
                address = "м. Арбатская",
                createdAt = Timestamp.now(),
                reward = "1000",
                category = "Документы",
                contact = "+7 968 123 45 67",
                imageUrl = "",
                isClosed = false
            )
        )
        Spacer(Modifier.height(50.dp))
        PostDetailsSheet(
            viewModel = TODO()
        )
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