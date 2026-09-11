package com.dorox.pinapp.presentation.screen.posts

import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorox.pinapp.domain.entity.Category
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.usecase.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.filter

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PostsState())
    val state = _state.asStateFlow()

    private val query = MutableStateFlow("")

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        loadPosts()
    }

    // Загрузка всех постов из БД Firebase
    private fun loadPosts() {
        viewModelScope.launch {
            if (_state.value.posts.isEmpty()) {
                _state.update {
                    it.copy(isLoading = true)
                }
            }
            try {
                val posts = getAllPostsUseCase()
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
                        error = e.message ?: "Ошибка загрузки"
                    )
                }
            }
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            _isRefreshing.value = true

            try {
                val posts = getAllPostsUseCase()
                _state.update {
                    it.copy(
                        posts = posts,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Ошибка обновления"
                    )
                }
            } finally {
                _isRefreshing.value = false
            }
        }
    }


    fun processCommand(command: PostsCommand) {

            when (command) {
                is PostsCommand.InputSearchQuery -> {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(query = command.query)
                        }
                    }
                }

                is PostsCommand.SelectCategory -> {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(selectedCategory = command.category)
                        }
                    }
                }
            }
    }

}

sealed interface PostsCommand {

    data class InputSearchQuery(val query: String) : PostsCommand
    data class SelectCategory(val category: Category) : PostsCommand
}

data class PostsState(
    val query: String = "",
    val selectedCategory: Category = Category.ALL,
    val posts: List<Post> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
) {

    val filteredPosts: List<Post>
        get() {
            return when(selectedCategory) {
                Category.ALL -> posts
                else -> posts.filter {
                    it.category == selectedCategory.text
                }
            }
        }
}
