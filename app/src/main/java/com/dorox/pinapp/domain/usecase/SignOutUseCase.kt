package com.dorox.pinapp.domain.usecase

import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.repository.AuthRepository
import com.dorox.pinapp.domain.repository.PostsRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator fun invoke(){
        return repository.signOut()
    }
}