package com.dorox.pinapp.domain.mapper

import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.presentation.screen.creation.CreatePostState


fun CreatePostState.Creation.toPost() : Post {
    return Post(
        title = this.title.replaceFirstChar { it.uppercaseChar() },
        description = this.description.replaceFirstChar { it.uppercaseChar() },
        status = this.status.text,
        category = this.category.text,
        contact = this.contact,
        address = this.address,
        imageUrl = this.imageUrl,
        reward = this.reward,
        isClosed = this.isClosed
    )
}


