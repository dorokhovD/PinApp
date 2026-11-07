package com.dorox.pinapp.domain.repository

import android.net.Uri
import com.dorox.pinapp.domain.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostsRepository {


    suspend fun createPost(post: Post, imageUri: Uri? = null): Result<String>

    //удаление поста
    suspend fun deletePost(postId: String, imageUrl: String)

    // редактирование поста
    suspend fun editPost(post: Post)

    // получение поста для отображения в ModalBottomSheet
    suspend fun getPostByPostId(postId: String) : Post // получение поста

    // получение постов по id пользователя и отображение в "моих объявлениях"
    suspend fun getPostsByUserId(userId: String?) : List<Post>

    suspend fun getAllPosts(): List<Post>
}