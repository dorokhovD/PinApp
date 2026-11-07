package com.dorox.pinapp.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorox.pinapp.domain.exception.AuthException
import com.dorox.pinapp.domain.usecase.RegisterUseCase
import com.dorox.pinapp.domain.usecase.ResetPasswordUseCase
import com.dorox.pinapp.domain.usecase.SignInUseCase
import com.dorox.pinapp.presentation.screen.auth.AuthState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val signInUseCase: SignInUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(Authorization())
    val state = _state.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun clearError() {
        _error.value = null
    }

    fun processCommand(command: AuthCommand) {
        when(command) {
            AuthCommand.Complete -> {
                // Если идет загрузка - вернуться
                if (_isLoading.value) return

                val currentState = _state.value
                if (currentState is Finished) return

                viewModelScope.launch {

                    _isLoading.value = true
                    _error.value = null

                    try {
                        when (currentState) {
                            is Authorization -> {
                                signInUseCase(
                                    email = currentState.email,
                                    password = currentState.password
                                )
                            }
                            is ResetPassword -> {
                               resetPasswordUseCase(
                                   email = currentState.email
                               )         
                            }

                            is Registration -> {
                                registerUseCase(
                                    displayName = currentState.displayName,
                                    email = currentState.email,
                                    password = currentState.password
                                )

                            }

                            is Finished -> return@launch

                        }
                        _state.update {
                            Finished
                        }
                    } catch (e: AuthException) {
                        _error.value = e.message
                    } catch (e: Exception) {
                        _error.value = e.message ?: "Неизвестная ошибка"
                    } finally {
                        _isLoading.value = false
                    }
                }
            }

            is AuthCommand.InputEmail -> {
                _state.update { prevState ->
                    when (prevState) {
                        is Authorization -> {
                            prevState.copy(email = command.email)
                        }
                        is Registration -> {
                            prevState.copy(email = command.email)
                        }
                        is ResetPassword -> {
                            prevState.copy(email = command.email)
                        }
                        else -> {
                            prevState
                        }
                    }
                }
            }

            is AuthCommand.InputPassword -> {
                _state.update { prevState ->
                    when (prevState) {
                        is Authorization -> {
                            prevState.copy(password = command.password)
                        }
                        is Registration -> {
                            prevState.copy(password = command.password)
                        }
                        else -> {
                            prevState
                        }
                    }
                }
            }

            is AuthCommand.InputDisplayName -> {
                _state.update { prevState ->
                    if (prevState is Registration) {
                        prevState.copy(displayName = command.displayName)
                    } else {
                        prevState
                    }
                }
            }

            is AuthCommand.SwitchMainState -> {
                _state.update { prevState ->
                    when(command.currentState) {
                        is Authorization -> Authorization()
                        is Registration -> Registration()
                        else -> prevState
                    }
                }
            }

            is AuthCommand.SwitchResetPasswordState -> {
                _state.update { prevState ->
                    when (command.currentState) {
                      is Authorization -> ResetPassword()
                      is ResetPassword -> Authorization()
                      else -> prevState
                    }
                }
            }

            AuthCommand.Back -> {
                _state.update {
                    Finished
                }
            }


        }
    }


}

sealed interface AuthCommand {

    data class InputDisplayName(val displayName: String) : AuthCommand

    data class InputEmail(val email: String) : AuthCommand

    data class InputPassword(val password: String) : AuthCommand

    data class SwitchMainState(val currentState: AuthState) : AuthCommand

    data class SwitchResetPasswordState(val currentState: AuthState) : AuthCommand

    data object Complete : AuthCommand

    data object Back : AuthCommand

}

sealed interface AuthState {

    data class Registration(
        val displayName: String = "",
        val email: String = "",
        val password: String = ""
    ) : AuthState


    data class Authorization(
        val email: String = "",
        val password: String = "",
    ) : AuthState

    data class ResetPassword(
        val email: String = "",
    ) : AuthState

    data object Finished : AuthState
}