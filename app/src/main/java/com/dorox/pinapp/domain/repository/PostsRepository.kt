package com.dorox.pinapp.domain.repository

import android.net.Uri
import com.dorox.pinapp.domain.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostsRepository {

    suspend fun createPost(post: Post, imageUri: Uri? = null): Result<String>

    // получение поста для отображения в ModalBottomSheet
    suspend fun getPostByPostId(postId: String) : Post // получение поста

    suspend fun getPostsByUserId(userId: String) : Flow<List<Post>>

    suspend fun getAllPosts(): List<Post>
}