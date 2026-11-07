package com.dorox.pinapp.domain.usecase

import android.net.Uri
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.repository.PostsRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository: PostsRepository
) {

    suspend operator fun invoke(postId: String, imageUrl: String) {
       return repository.deletePost(postId = postId, imageUrl = imageUrl)
    }
}