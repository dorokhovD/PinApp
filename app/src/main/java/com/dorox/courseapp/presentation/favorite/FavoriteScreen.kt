package com.dorox.courseapp.presentation.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dorox.courseapp.presentation.main.CourseCard
import com.dorox.courseapp.presentation.main.CourseViewModel
import com.dorox.courseapp.presentation.main.imageList

@Composable
fun FavoriteScreen(
    paddingValues: PaddingValues,
    viewModel: CourseViewModel
) {

    val state by viewModel.favoriteCoursesState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = paddingValues
    ) {
        item {
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = "Избранное",
                fontSize = 24.sp
            )
        }

        items(items = state.favoriteCourses) {course ->
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
