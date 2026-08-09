package com.dorox.pinapp.domain.repository

import com.dorox.pinapp.domain.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    suspend fun createPost(post: Post)

    // получение поста для отображения в BottomSheet
    suspend fun getPostByPostId(postId: String) : Post // получение поста

    // Получение
    suspend fun getPostsByUserId(userId: String) : Flow<List<Post>>

    fun getAllPosts(): Flow<List<Post>>
}