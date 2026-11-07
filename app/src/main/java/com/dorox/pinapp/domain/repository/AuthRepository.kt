package com.dorox.pinapp.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    fun getCurrentUser() : FirebaseUser?

    fun getCurrentUserUid() : String?

    suspend fun provideAuthenticated(): String

    suspend fun signInAnonymously(): String

    suspend fun signIn(email: String, password: String): String

    suspend fun signUp(displayName: String, email: String, password: String): String

    suspend fun resetPassword(email: String)

    fun isAnonymous(): Boolean

    fun signOut()

}