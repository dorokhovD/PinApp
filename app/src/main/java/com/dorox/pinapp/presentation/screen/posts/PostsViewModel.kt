package com.dorox.pinapp.presentation.screen.posts

import android.util.Log
import androidx.compose.ui.window.isPopupLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorox.pinapp.data.repository.FireBasePostRepository
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.usecase.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.PrivateKey
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PostsState())
    val state = _state.asStateFlow()

    private val query = MutableStateFlow("")

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            getAllPostsUseCase()
                .collect { postsList ->
                    _state.update {
                        it.copy(posts = postsList)
                    }
                }
        }
    }
    fun processCommand(command: PostsCommand) {
        viewModelScope.launch {
            when (command) {
                is PostsCommand.InputSearchQuery -> {
                    query
                }
            }
        }
    }

}

sealed interface PostsCommand {

    data class InputSearchQuery(val query: String) : PostsCommand

}

data class PostsState(
    val query: String = "",
    val posts: List<Post> = emptyList()
)
