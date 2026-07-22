package com.dorox.courseapp.domain

import com.dorox.courseapp.data.CoursesApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoursesRepository @Inject constructor(
    private val api: CoursesApi
) {

    suspend fun getCourses(): List<Course> {
        return api.getCourses().courses
    }
}