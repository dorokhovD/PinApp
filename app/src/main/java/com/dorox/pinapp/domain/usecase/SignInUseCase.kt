package com.dorox.pinapp.domain.usecase

import com.dorox.pinapp.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String) : String {
        return repository.signIn(
            email = email,
            password = password
        )
    }
}