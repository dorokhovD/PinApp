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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.dorox.pinapp.R
import com.dorox.pinapp.domain.entity.Category
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.entity.Status
import com.dorox.pinapp.presentation.utils.toDateString
import com.dorox.pinapp.ui.theme.Black100
import com.dorox.pinapp.ui.theme.Gray100
import com.dorox.pinapp.ui.theme.Gray200
import com.dorox.pinapp.ui.theme.Gray300
import com.dorox.pinapp.ui.theme.Gray400
import com.dorox.pinapp.ui.theme.Gray50
import com.dorox.pinapp.ui.theme.Gray500
import com.dorox.pinapp.ui.theme.Green100
import com.dorox.pinapp.ui.theme.Green200
import com.dorox.pinapp.ui.theme.Orange100
import com.dorox.pinapp.ui.theme.Orange200
import com.dorox.pinapp.ui.theme.Red100
import com.dorox.pinapp.ui.theme.Red200
import com.dorox.pinapp.ui.theme.White100
import com.google.firebase.Timestamp
import com.google.firebase.annotations.concurrent.Background

@Composable
fun PostsScreen(
    innerPadding: PaddingValues,
    viewModel: PostsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Gray400).padding(horizontal = 16.dp),
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
        ) {

            PostCard(
                onPostClick = {

                },
                post = it,
            )

        }
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
                    text = post.status,
                    colorText = statusTextColor,
                    colorBackground = statusBackgroundColor,
                )

                InfoBoxText(
                    text = post.createdAt.toDateString(),
                    colorText = Gray500,
                    colorBackground = Color.Transparent,
                    horizontalDp = 0.dp,
                    contentAlignment = Alignment.TopEnd
                )

            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = post.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Black100
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = post.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Black100,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    lineHeight = 21.sp
                )
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoBoxText(
                    text = post.address,
                    colorText = Gray500,
                    colorBackground = Color.Transparent,
                    horizontalDp = 0.dp,
                )


                if (post.reward.isNotEmpty()) {
                    InfoBoxText(
                        text = "${post.reward} ₽",
                        colorText = Gray500,
                        colorBackground = Orange100,
                        contentAlignment = Alignment.TopEnd
                    )
                }
            }
        }
    }
}

@Composable
fun LongInfoPostCard(
    modifier: Modifier = Modifier,
    title: String = "Связка ключей с красным брелоком",
    description: String = "Потерял в районе Арбата в понедельник, в связке было четыре ключа",
    imageUrl: String = "",
    status: String = Status.LOST.text,
    category: String = Category.DOCUMENTS.text,
    contact: String = "+7 905 123 45 67",
    address: String = "м. Арбатская",
    createdAt: String = "26 июл"

) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val statusBackgroundColor = if (status == Status.FOUND.text) Green100 else Red100
            val statusTextColor = if (status == Status.FOUND.text) Green200 else Red200

            Row() {
                InfoBoxText(
                    text = status,
                    colorText = statusTextColor,
                    colorBackground = statusBackgroundColor,
                )

                Spacer(Modifier.width(4.dp))

                InfoBoxText(
                    text = category,
                    colorText = Gray500,
                    colorBackground = Gray200,
                )
            }

            InfoBoxText(
                text = createdAt,
                colorText = Gray500,
                colorBackground = Color.Transparent,
                horizontalDp = 0.dp,
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Black100
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = description,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Black100
        )

        if (imageUrl.isNotEmpty()) {
            Box(
                modifier = modifier
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
            Spacer(Modifier.height(8.dp))
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            thickness = 2.dp,
            color = Gray200
        )

        InfoBoxTextWithIcon(
            titleInfo = "Место",
            descriptionInfo = address,
            imageVector = ImageVector.vectorResource(R.drawable.ic_location_pin)
        )

        Spacer(Modifier.height(16.dp))

        InfoBoxTextWithIcon(
            titleInfo = "Контакт",
            descriptionInfo = contact,
            imageVector = ImageVector.vectorResource(R.drawable.ic_call)
        )

        Spacer(Modifier.height(24.dp))

        CallTextButton(
            onCallClick = {

            }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun InfoBoxText(
    modifier: Modifier = Modifier,
    text: String,
    colorText: Color,
    colorBackground: Color,
    contentAlignment: Alignment = Alignment.TopStart,
    horizontalDp: Dp = 10.dp,
    verticalDp: Dp = 4.dp
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color = colorBackground)
            .padding(horizontal = horizontalDp, vertical = verticalDp),
        contentAlignment = contentAlignment
    ) {
        Text(
            text = text,
            color = colorText,
            fontSize = 12.sp
        )
    }

}

@Composable
private fun CallTextButton(
    modifier: Modifier = Modifier,
    onCallClick: () -> Unit
) {
    TextButton(
        modifier = modifier
            .fillMaxWidth(),
        shape = CircleShape,
        onClick = {
            onCallClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange200,
            contentColor = White100,
            disabledContentColor = Gray200,
            disabledContainerColor = Gray50
        ),
        elevation = buttonElevation(
            defaultElevation = 2.dp
        )

    ) {
        Text(
            text = "Связаться с автором",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun InfoBoxTextWithIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    titleInfo: String,
    descriptionInfo: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Gray200,
                shape = RoundedCornerShape(16.dp)
            )
            
    ) {
        Row(
            modifier = modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = Gray100
            )
            Column {
                Text(
                    text = titleInfo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Gray500
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = descriptionInfo,
                    fontSize = 14.sp,
                    color = Black100
                )
            }
        }
    }

}

@Preview
@Composable
fun PreviewPostCard(
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
        LongInfoPostCard()
    }

}

@Composable
private fun SearchOutlinedTextField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {
    var textFieldState = rememberTextFieldState(query)

    OutlinedTextField(
        modifier = Modifier
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