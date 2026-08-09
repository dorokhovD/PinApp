package com.dorox.pinapp.presentation.screen.creation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorox.pinapp.domain.entity.Category
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.entity.Status
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
                    }else {
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
                            addPostUseCase(post = prevState.toPost())
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
        val isClosed: Boolean = false
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

        fun toPost() : Post {
            return Post(
                title = title,
                description = description,
                status = status.text,
                category = category.text,
                contact = contact,
                address = address,
                imageUrl = imageUrl,
                reward = reward,
                isClosed = false
            )
        }
    }
    data object Finished : CreatePostState
}

