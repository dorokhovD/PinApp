package com.dorox.courseapp.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorox.courseapp.domain.Course
import com.dorox.courseapp.domain.CoursesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val repository: CoursesRepository
) : ViewModel() {

    private val _coursesState = MutableStateFlow(CoursesState())
    val coursesState = _coursesState.asStateFlow()

    private val _favoriteCoursesState = MutableStateFlow(FavoriteCoursesState())
    val favoriteCoursesState = _favoriteCoursesState.asStateFlow()

    init {
        loadCourses()
    }

    private fun loadCourses() {
        viewModelScope.launch {
            try {
                val courses = repository.getCourses()
                _coursesState.value = CoursesState(courses = courses)
                updateFavorite()
            } catch (e: Exception) {
                Log.d("loadCourses", e.toString())
            }
        }
    }

    private fun updateFavorite() {
        val favoriteCourses = _coursesState.value.courses.filter {
            it.hasLike
        }
        _favoriteCoursesState.value = FavoriteCoursesState(favoriteCourses = favoriteCourses)
    }

    fun changeFavorite(courseId: Int) {
        _coursesState.update { oldState ->
            val newCourses = oldState.courses.map { course ->
                if (course.id == courseId) {
                    course.copy(hasLike = !course.hasLike)
                } else {
                    course
                }
            }
            oldState.copy(courses = newCourses)
        }
        updateFavorite()
    }

    fun sortByDate() {
        _coursesState.update { oldState ->
            val sortedCourses = if (!oldState.isSortedByDate) {
                oldState.courses.sortedByDescending { course ->
                    course.publishDate
                }
            } else {
                oldState.courses.sortedBy { course ->
                    course.id
                }
            }
            oldState.copy(
                courses = sortedCourses,
                isSortedByDate = !oldState.isSortedByDate
            )
        }
    }
}

data class CoursesState(
    val courses: List<Course> = listOf(),
    val isSortedByDate: Boolean = false
)

data class FavoriteCoursesState(
    val favoriteCourses: List<Course> = listOf()
)