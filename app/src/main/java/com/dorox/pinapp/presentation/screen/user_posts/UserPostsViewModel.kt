package com.dorox.pinapp.presentation.screen.user_posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.usecase.DeletePostUseCase
import com.dorox.pinapp.domain.usecase.GetCurrentUserUidUseCase
import com.dorox.pinapp.domain.usecase.GetUserPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPostsViewModel @Inject constructor(
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getCurrentUserUidUseCase: GetCurrentUserUidUseCase,
    private val deletePostUseCase: DeletePostUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserPostsState())
    val state = _state.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        loadUserPosts()
    }

    private fun loadUserPosts() {
        viewModelScope.launch {
            if (_state.value.posts.isEmpty()) {
                _state.update {
                    it.copy(isLoading = true)
                }
            }
            try {
                val userId = getCurrentUserUidUseCase()
                val posts = getUserPostsUseCase(userId = userId)
                _state.update {
                    it.copy(
                        posts = posts,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Ошибка загрузки постов Пользователя"
                    )
                }
            }
        }
    }

    fun refreshUserPosts() {
        viewModelScope.launch {
            _isRefreshing.value = true

            try {
                val userId = getCurrentUserUidUseCase()
                val posts = getUserPostsUseCase(userId = userId)
                _state.update {
                    it.copy(
                        posts = posts,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Ошибка обновления постов пользователя"
                    )
                }
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun deletePost(postId: String, imageUrl: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    deletedPostId = postId,
                    error = null
                )
            }
            try {
                deletePostUseCase(
                    postId = postId,
                    imageUrl = imageUrl
                )
                _state.update {prevState ->
                    prevState.copy(
                        posts = prevState.posts.filterNot { it.postId == postId },
                        isLoading = false,
                        deletedPostId = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        deletedPostId = null,
                        error = e.message ?: "Ошибка при попытке удаления объявления"
                    )
                }
            }
        }
    }

    fun processCommand(command: UserPostsCommand) {
        when (command) {
            is UserPostsCommand.DeletePost ->
                deletePost(postId = command.postId, imageUrl = command.imageUrl)

            is UserPostsCommand.EditPost ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(editedPostId = command.postId)
                    }
                }
        }
    }
}

sealed interface UserPostsCommand {

    data class DeletePost(val postId: String, val imageUrl: String) : UserPostsCommand
    data class EditPost(val postId: String) : UserPostsCommand

}

data class UserPostsState(
    val posts: List<Post> = emptyList(),
    val deletedPostId: String? = null,
    val editedPostId: String? = null,
    val error: String? = null,
    val isLoading: Boolean = false
) {

}