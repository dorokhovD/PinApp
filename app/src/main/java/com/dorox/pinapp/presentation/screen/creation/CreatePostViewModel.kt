package com.dorox.pinapp.presentation.screen.creation

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorox.pinapp.domain.entity.Category
import com.dorox.pinapp.domain.entity.Status
import com.dorox.pinapp.domain.mapper.toPost
import com.dorox.pinapp.domain.usecase.CreatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase
)  : ViewModel() {

    private val _state = MutableStateFlow<CreatePostState>(CreatePostState.Creation())
    val state = _state.asStateFlow()

    fun processCommand(command: CreatePostCommand) {
        when(command) {
            CreatePostCommand.Back -> {
                _state.update {
                    CreatePostState.Finished
                }
            }

            is CreatePostCommand.AddImage -> {
                _state.update { prevState ->
                    if (prevState is CreatePostState.Creation) {
                        prevState.copy(imageUrl = command.uri.toString())
                    } else {
                        prevState
                    }
                }
            }

            is CreatePostCommand.DeleteImage -> {
                _state.update { prevState ->
                    if (prevState is CreatePostState.Creation) {
                        prevState.copy(imageUrl = "")
                    } else {
                        prevState
                    }
                }
            }

            is CreatePostCommand.InputAddress -> {
                _state.update { prevState ->
                    if (prevState is CreatePostState.Creation) {
                        prevState.copy(address = command.address)
                    } else {
                        prevState
                    }
                }
            }
            is CreatePostCommand.InputDescription -> {
                _state.update { prevState ->
                    if (prevState is CreatePostState.Creation) {
                        prevState.copy(description = command.description)
                    } else {
                        prevState
                    }
                }
            }
            is CreatePostCommand.InputContact -> {
                _state.update { prevState ->
                    if (prevState is CreatePostState.Creation) {
                        prevState.copy(contact = command.contact)
                    } else {
                        prevState
                    }
                }
            }
            is CreatePostCommand.InputTitle -> {
                _state.update { prevState ->
                    if (prevState is CreatePostState.Creation) {
                        prevState.copy(title = command.title)
                    } else {
                        prevState
                    }
                }
            }

            is CreatePostCommand.SelectCategory -> {
                _state.update { prevState ->
                    if (prevState is CreatePostState.Creation) {
                        prevState.copy(category = command.category)
                    } else {
                        prevState
                    }
                }
            }
            is CreatePostCommand.SelectStatus -> {
                _state.update { prevState ->
                    if (prevState is CreatePostState.Creation) {
                        prevState.copy(status = command.status)
                    } else {
                        prevState
                    }
                }
            }

            is CreatePostCommand.InputReward -> {
                _state.update { prevState ->
                    if (prevState is CreatePostState.Creation) {
                        prevState.copy(reward = command.reward)
                    } else {
                        prevState
                    }

                }
            }
            CreatePostCommand.Save -> {
                val currentState = _state.value

                // Если текущий стейт не Creation - вернуться
                if (currentState !is CreatePostState.Creation) return

                // Если статус загрузки true - вернуться
                if (currentState.isLoading) return
                savePost(currentState)
            }
        }
    }
    private fun savePost(state: CreatePostState.Creation) {
        viewModelScope.launch {
            // Меняет статус загрузки на true и сбрасывает ошибку
            _state.update { prevState ->
                if (prevState is CreatePostState.Creation) {
                    prevState.copy(
                        isLoading = true,
                        error = null
                    )
                } else {
                    prevState
                }
            }
            val post = state.toPost()
            val imageUri = state.imageUrl.toUri()
            val result = createPostUseCase(
                post = post,
                imageUri = imageUri
            )
            _state.update { prevState ->
                if (result.isSuccess) {
                    CreatePostState.Finished
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Ошибка при сохранении"
                    if (prevState is CreatePostState.Creation) {
                        prevState.copy(
                            isLoading = false,
                            error = error
                        )
                    } else {
                        prevState
                    }
                }
            }
        }
    }
}

sealed interface CreatePostCommand {

    data class SelectStatus(val status: Status) : CreatePostCommand

    data class SelectCategory(val category: Category) : CreatePostCommand

    data class InputTitle(val title: String) : CreatePostCommand

    data class InputDescription(val description: String) : CreatePostCommand

    data class InputContact(val contact: String) : CreatePostCommand

    data class InputAddress(val address: String) : CreatePostCommand

    data class AddImage(val uri: Uri) : CreatePostCommand

    data class InputReward(val reward: String) : CreatePostCommand

    data object DeleteImage : CreatePostCommand

    data object Save : CreatePostCommand

    data object Back : CreatePostCommand

}

sealed interface CreatePostState {

    data class Creation(
        val title: String = "",
        val description: String = "",
        val status: Status = Status.NOT_SELECTED,
        val category: Category = Category.NOT_SELECTED,
        val contact: String = "",
        val address: String = "",
        val imageUrl: String = "",
        val reward: String = "",
        val isClosed: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : CreatePostState {

        val isSaveEnable: Boolean
            get() = title.isNotBlank() &&
                    status.text.isNotBlank() &&
                    category.text.isNotBlank() &&
                    contact.isNotBlank() &&
                    address.isNotBlank()
    }
    data object Finished : CreatePostState
}

