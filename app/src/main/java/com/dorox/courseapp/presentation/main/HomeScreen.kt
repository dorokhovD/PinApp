package com.dorox.courseapp.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dorox.courseapp.R
import com.dorox.courseapp.domain.Course
import com.dorox.courseapp.ui.theme.Glass
import com.dorox.courseapp.ui.theme.Gray100
import com.dorox.courseapp.ui.theme.Gray200
import com.dorox.courseapp.ui.theme.Gray400
import com.dorox.courseapp.ui.theme.Green100
import com.dorox.courseapp.ui.theme.White100
import com.dorox.courseapp.ui.theme.White200

val imageList = listOf(
    R.drawable.cover1,
    R.drawable.cover2,
    R.drawable.cover3
)

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    viewModel: CourseViewModel
) {

    val state by viewModel.coursesState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = paddingValues
    ) {
        item {
            Row {
                TextField(
                    modifier = Modifier.weight(1f),
                    state = rememberTextFieldState(),
                    placeholder = {
                        Text(
                            text = "Search courses...",
                            color = Gray100,
                        )
                    },
                    shape = RoundedCornerShape(32.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = White100,
                        unfocusedTextColor = Gray100,
                        focusedContainerColor = Gray200,
                        unfocusedContainerColor = Gray200
                    ),
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.padding(start = 8.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                            tint = White100,
                            contentDescription = null
                        )
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    modifier = Modifier.size(55.dp),
                    onClick = {},
                    colors = IconButtonColors(
                        containerColor = Gray200,
                        contentColor = White100,
                        disabledContainerColor = Gray200,
                        disabledContentColor = White100
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                        contentDescription = null
                    )
                }
            }

        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        viewModel.sortByDate()
                    }
                ) {
                    Text(
                        text = "По дате добавления",
                        color = Green100,
                        textAlign = TextAlign.End,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_sorted),
                        contentDescription = null,
                        tint = Green100
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(items = state.courses) { course ->
            val imageIndex = course.id % imageList.size
            val imageRes = imageList[imageIndex]

            CourseCard(
                course = course,
                imageId = imageRes,
                onFavoriteButtonClick = {
                    viewModel.changeFavorite(course.id)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    imageId: Int,
    onFavoriteButtonClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Gray400,
            contentColor = White100
        )
    ) {
        Box {
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painterResource(id = imageId),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )
            IconButton(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
                    .size(36.dp),
                onClick = {
                    onFavoriteButtonClick()
                },
                colors = IconButtonColors(
                    containerColor = Glass,
                    contentColor = White100,
                    disabledContainerColor = Glass,
                    disabledContentColor = White100
                ),
                shape = CircleShape
            ) {
                if (course.hasLike) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_favorite_fill),
                        contentDescription = null,
                        tint = Green100
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_favorite),
                        contentDescription = null,
                        tint = White100
                    )
                }

            }
            Row(
                modifier = Modifier.align(Alignment.BottomStart).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier.background(
                            color = Glass,
                            shape = CircleShape
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                            contentDescription = null,
                            tint = Green100
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = course.rate,
                            fontSize = 14.sp
                        )
                    }
                }
                Box(
                    modifier = Modifier.background(
                        color = Glass,
                        shape = CircleShape
                    )
                ){
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 1.dp),
                        text = formatDateWithMonth(course.startDate) ,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = course.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = course.text,
                fontSize = 14.sp,
                color = White200,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = 0.4.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${course.price} ₽",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                )
                Row {
                    TextButton(
                        onClick = {},
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Подробнее",
                            fontSize = 12.sp,
                            color = Green100
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                            contentDescription = null,
                            tint = Green100
                        )
                    }
                }
            }
        }
    }
}