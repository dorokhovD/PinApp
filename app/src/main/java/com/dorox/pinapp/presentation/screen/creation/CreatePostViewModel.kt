package com.dorox.pinapp.presentation.screen.creation

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.toUri
import com.dorox.pinapp.domain.entity.Category
import com.dorox.pinapp.domain.entity.Status
import com.dorox.pinapp.domain.mapper.CreatePostMapper
import com.dorox.pinapp.domain.usecase.AddPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase
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
                        prevState.copy(imageUri = command.uri.toString())
                    } else {
                        prevState
                    }
                }
            }

            is CreatePostCommand.DeleteImage -> {
                _state.update { prevState ->
                    if (prevState is CreatePostState.Creation) {
                        prevState.copy(imageUri = null)
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
            CreatePostCommand.Save -> {
                viewModelScope.launch {
                    _state.update { prevState ->
                        if (prevState is CreatePostState.Creation) {
                            val post = CreatePostMapper.toPost(prevState)
                            addPostUseCase(
                                post = post,
                                imageUri = post.imageUrl?.toUri()
                            )
                            CreatePostState.Finished
                        } else {
                            prevState
                        }
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
        val imageUri: String? = null,
        val reward: String = "",
        val isClosed: Boolean = false,
    ) : CreatePostState {

        val isSaveEnable: Boolean
            get() {
                return when {
                    title.isBlank() -> false
                    status.text.isBlank() -> false
                    category.text.isBlank() -> false
                    contact.isBlank() -> false
                    address.isBlank() -> false
                    else -> {
                        true
                    }
                }
            }
    }
    data object Finished : CreatePostState
}

