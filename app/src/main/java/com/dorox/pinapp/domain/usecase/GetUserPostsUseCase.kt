package com.dorox.pinapp.domain.usecase

import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.repository.PostsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPostsUseCase @Inject constructor(
    private val repository: PostsRepository
) {

    suspend operator fun invoke(userId: String?): List<Post> {
       return repository.getPostsByUserId(userId = userId)
    }
}