package com.dorox.pinapp.domain.usecase

import com.dorox.pinapp.data.repository.FireBasePostRepository
import com.dorox.pinapp.domain.entity.Post
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPostsUseCase @Inject constructor(
    private val repository: FireBasePostRepository
) {

    operator fun invoke(): Flow<List<Post>> {
       return repository.getAllPosts()
    }
}