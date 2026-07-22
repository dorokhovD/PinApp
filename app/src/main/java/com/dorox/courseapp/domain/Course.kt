package com.dorox.courseapp.domain

data class Course(
    val id: Int,
    val title: String,
    val text: String,
    val price: String,
    val rate: String,
    val startDate: String,
    val hasLike: Boolean,
    val publishDate: String
)

data class CoursesResponse(
    val courses: List<Course>
)

