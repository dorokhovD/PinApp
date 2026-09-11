package com.dorox.pinapp.domain.usecase

import android.net.Uri
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.repository.PostsRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: PostsRepository
) {

    suspend operator fun invoke(post: Post, imageUri: Uri? = null): Result<String> {
       return repository.createPost(post = post, imageUri = imageUri)
    }
}