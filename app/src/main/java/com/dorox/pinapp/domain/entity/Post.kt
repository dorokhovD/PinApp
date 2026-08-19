package com.dorox.pinapp.domain.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Post(
    val postId: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val status: String = "",
    val category: String = "",
    val contact: String = "",
    val address: String = "",
    val imageUrl: String? = "",
    val reward: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    @PropertyName("closed")
    val isClosed: Boolean = false
)