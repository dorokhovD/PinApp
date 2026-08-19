package com.dorox.pinapp.presentation.screen.postdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.usecase.GetPostDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val getPostDetailsUseCase: GetPostDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PostDetailsState())
    val state = _state.asStateFlow()

    fun loadPostDetails(postId: String) {
        viewModelScope.launch {
            _state.update {
                val post = getPostDetailsUseCase(postId = postId)
                it.copy(post = post)
            }

        }
    }
    fun clearPostDetails() {
        _state.value = PostDetailsState()
    }
}

data class PostDetailsState(
    val post: Post = Post()
)

