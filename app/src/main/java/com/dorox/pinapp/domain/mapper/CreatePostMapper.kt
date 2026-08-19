package com.dorox.pinapp.domain.mapper

import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.presentation.screen.creation.CreatePostState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object CreatePostMapper {
    fun toPost(state: CreatePostState.Creation) : Post {
        return Post(
            title = state.title,
            description = state.description,
            status = state.status.text,
            category = state.category.text,
            contact = state.contact,
            address = state.address,
            imageUrl = state.imageUri,
            reward = state.reward,
            isClosed = state.isClosed
        )
    }
}

