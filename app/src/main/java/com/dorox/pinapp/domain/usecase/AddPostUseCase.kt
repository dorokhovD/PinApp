package com.dorox.pinapp.domain.usecase

import com.dorox.pinapp.data.repository.FireBasePostRepository
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.entity.Status
import javax.inject.Inject

class AddPostUseCase @Inject constructor(
    private val repository: FireBasePostRepository
) {

    suspend operator fun invoke(post: Post) {
        repository.createPost(post = post)
    }
}