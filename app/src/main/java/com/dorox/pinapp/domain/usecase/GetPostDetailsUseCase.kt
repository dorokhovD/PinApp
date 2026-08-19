package com.dorox.pinapp.domain.usecase

import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.repository.PostsRepository
import javax.inject.Inject

class GetPostDetailsUseCase @Inject constructor(
    private val repository: PostsRepository
) {

    suspend operator fun invoke(postId: String): Post {
        return repository.getPostByPostId(postId = postId)
    }
}