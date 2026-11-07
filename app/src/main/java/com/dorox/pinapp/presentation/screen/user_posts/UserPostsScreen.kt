package com.dorox.pinapp.presentation.screen.user_posts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.dorox.pinapp.R
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.entity.Status
import com.dorox.pinapp.presentation.utils.toLongDateString
import com.dorox.pinapp.ui.theme.Black100
import com.dorox.pinapp.ui.theme.Blue50
import com.dorox.pinapp.ui.theme.CustomAlertDialog
import com.dorox.pinapp.ui.theme.CustomTextButton
import com.dorox.pinapp.ui.theme.ErrorButton
import com.dorox.pinapp.ui.theme.Gray500
import com.dorox.pinapp.ui.theme.Green100
import com.dorox.pinapp.ui.theme.Green200
import com.dorox.pinapp.ui.theme.LoadingIndicator
import com.dorox.pinapp.ui.theme.Orange300
import com.dorox.pinapp.ui.theme.Orange5
import com.dorox.pinapp.ui.theme.PinAppTheme
import com.dorox.pinapp.ui.theme.Red100
import com.dorox.pinapp.ui.theme.Red200
import com.dorox.pinapp.ui.theme.TextBoxWithStartIcon
import com.dorox.pinapp.ui.theme.White100
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersPostsScreen(
    viewModel: UserPostsViewModel = hiltViewModel(),
    onBackIconClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val openDialog = remember { mutableStateOf(false) }

    val deletedPostId = remember { mutableStateOf("") }

    val deletedImageUrl = remember { mutableStateOf("") }


//    LaunchedEffect(state.error) {
//        state.error?.let {
//        }
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackIconClick()
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                            contentDescription = null,
                            tint = Black100
                        )
                    }
                },
                title = {
                    Text(text = "Мои объявления")
                }
            )
        }
    ) { innerPadding ->

        if (openDialog.value) {
            CustomAlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = "Вы действительно хотите удалить выбранное объявление?",
                onCancelClick = {
                    openDialog.value = false
                },
                onConfirmClick = {
                    openDialog.value = false
                    viewModel.processCommand(
                        UserPostsCommand.DeletePost(
                            postId = deletedPostId.value,
                            imageUrl = deletedImageUrl.value
                        )
                    )
                    viewModel.refreshUserPosts()
                }
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = Orange5),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.posts,
                key = {it.postId }
            ) { post ->


                UserPostCard(
                    modifier = Modifier.animateItem(),
                    post = post,
                    onEditButtonClick = { postId ->
                        viewModel.processCommand(UserPostsCommand.EditPost(postId = postId))
                    },
                    onDeleteButtonClick = { postId, imageUrl ->
                        deletedPostId.value = postId
                        deletedImageUrl.value = imageUrl
                        openDialog.value = true
                    }
                )
            }

            if (state.posts.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                        text = "Нет созданных объявлений",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }

            }

            if (state.isLoading && state.posts.isEmpty()) {
                item {
                    LoadingIndicator()
                }
            }

            if (state.error != null && state.posts.isEmpty()) {
                item {
                    ErrorButton(
                        errorText = state.error.toString(),
                        onRefreshButtonClick = {
                            viewModel.refreshUserPosts()
                        }
                    )
                }
            }
        }



    }
}

@Composable
private fun UserPostCard(
    modifier: Modifier = Modifier,
    post: Post,
    onEditButtonClick: (String) -> Unit,
    onDeleteButtonClick: (String, String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        elevation = CardDefaults.cardElevation(4.dp,),
        colors = CardDefaults.cardColors(
            containerColor = White100
        ),
        shape = RoundedCornerShape(24.dp),
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
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
                    text = post.createdAt.toLongDateString(),
                    colorText = Gray500,
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

            Spacer(Modifier.height(4.dp))

            Text(
                text = post.description,
                style = MaterialTheme.typography.bodyMedium,
            )

            if (post.imageUrl != "") {

                Spacer(Modifier.height(8.dp))

                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(containerColor = Orange5)
                ) {
                    val isPreview = LocalInspectionMode.current
                    if (isPreview) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(250.dp)
                                .clip(RoundedCornerShape(24.dp)),
                            painter = painterResource(id = R.drawable.y1),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        AsyncImage(
                            modifier = Modifier
                                .heightIn(max = 250.dp)
                                .align(alignment = Alignment.CenterHorizontally),
                            //.clip(RoundedCornerShape(16.dp)),
                            model = post.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            placeholder = painterResource(R.drawable.ic_photo_250)
                        )
                    }

                }
            }

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

            TextBoxWithStartIcon(
                text = post.contact,
                colorText = Black100,
                colorBackground = Color.Transparent,
                horizontalDp = 0.dp,
                icon = ImageVector.vectorResource(R.drawable.ic_account_circle_24),
                tint = Black100
            )

            if (post.reward.isNotEmpty()) {

                Spacer(Modifier.height(8.dp))

                TextBoxWithStartIcon(
                    text = stringResource(R.string.reward, post.reward),
                    colorText = Orange300,
                    //colorBackground = Orange10,
                    colorBackground = Color.Transparent,
                    horizontalDp = 0.dp,
                    icon = ImageVector.vectorResource(R.drawable.ic_money_bag),
                    tint = Orange300
                )
            }

        }
    }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomTextButton(
                modifier = Modifier.weight(1f),
                containerColor = Blue50,
                onClick = {
                    onEditButtonClick(post.postId)
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))

                Text("Редактировать")
            }

            Spacer(Modifier.width(16.dp))

            CustomTextButton(
                modifier = Modifier.weight(1f),
                containerColor = Red200.copy(alpha = 0.7f),
                onClick = {
                    onDeleteButtonClick(post.postId, post.imageUrl)
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))

                Text("Удалить")
            }
        }
    }
}

@Preview
@Composable
fun UserPostCardPreview() {
    PinAppTheme() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Gray500)
        ) {
            item {
                UserPostCard(
                    post = Post(
                        postId = "123",
                        userId = "43251354",
                        title = "Ноутбук Macbook",
                        description = "Потерялся макбук около метро Семеновская",
                        status = "Потеряно",
                        category = "Ноутбуки",
                        contact = "+79680842521",
                        address = "Москва, м. Семеновская",
                        imageUrl = "https://firebasestorage.googleapis.com/v0/b/pinapp-a8970.firebasestorage.app/o/830af9df-d53d-4e5a-bd6e-fba357ab59ed.jpg?alt=media&token=65fe5d8f-c5d9-4abb-9077-5cdd8e5eeae1",
                        reward = "10000",
                        createdAt = Timestamp.now(),
                        isClosed = false
                    ),
                    onEditButtonClick = {},
                    onDeleteButtonClick = {

                    } as (String, String?) -> Unit
                )
            }

        }
    }


}