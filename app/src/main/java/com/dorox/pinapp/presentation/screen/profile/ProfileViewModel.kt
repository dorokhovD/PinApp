package com.dorox.pinapp.presentation.screen.profile

import androidx.lifecycle.ViewModel
import com.dorox.pinapp.domain.usecase.GetCurrentUserUseCase
import com.dorox.pinapp.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    //private val authRepository: AuthRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase
): ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Anonymous)
    val state = _state.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val user = getCurrentUserUseCase()
        if (user == null || user.isAnonymous) {
            _state.update {
                ProfileState.Anonymous
            }
        } else {
            _state.update {
                ProfileState.Authorized(
                    email = user.email ?: "",
                    displayName = user.displayName ?: "",
                    uid = user.uid
                )
            }
        }
    }

    fun signOut() {
        signOutUseCase()
        _state.update {
            ProfileState.Anonymous
        }
    }

    fun checkAuthorized() {
        loadCurrentUser()
    }

    fun onAuthorizedSuccess() {
        loadCurrentUser()
    }

}

sealed class ProfileState {

    data class Authorized(
        val email: String = "",
        val displayName: String = "",
        val uid: String = ""
    ) : ProfileState()

    data object Anonymous : ProfileState()
}

