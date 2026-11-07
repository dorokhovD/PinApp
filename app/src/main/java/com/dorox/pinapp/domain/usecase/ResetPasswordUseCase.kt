package com.dorox.pinapp.domain.usecase

import com.dorox.pinapp.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(email: String) {
        return repository.resetPassword(email = email)
    }
}