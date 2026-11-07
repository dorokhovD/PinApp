package com.dorox.pinapp.presentation.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorox.pinapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isAuth = MutableStateFlow(false)
    val isAuth = _isAuth.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val uid = authRepository.provideAuthenticated()
                Log.d("Auth", "Авторизован с UID: $uid")
            } catch (e: Exception) {
                Log.e("Auth", "Ошибка авторизации", e)
            } finally {
                _isAuth.value = true
            }
        }
    }
}