package com.example.userslistapp.presentation.user_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userslistapp.data.repository.UsersRepository
import com.example.userslistapp.data.repository.UsersState
import com.example.userslistapp.presentation.mapper.toUser
import com.example.userslistapp.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val repository: UsersRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<UserDetailsState>(UserDetailsState.Loading)
    val state: StateFlow<UserDetailsState> = _state.asStateFlow()

    init {
        savedStateHandle.get<Int>("user_id")?.let { userId ->
            loadUser(userId)
        }
    }

    private fun loadUser(userId: Int) {
        viewModelScope.launch {
            repository.getUsers().collect { state ->
                when (state) {
                    is UsersState.Loading -> {
                        _state.value = UserDetailsState.Loading
                    }

                    is UsersState.Success -> {
                        val user = state.data.find { it.id == userId }?.toUser()
                        user?.let {
                            _state.value = UserDetailsState.Success(it)
                        } ?: run {
                            _state.value = UserDetailsState.Error("User not found")
                        }
                    }

                    is UsersState.Error -> {
                        _state.value = UserDetailsState.Error(state.message)
                    }
                }
            }
        }
    }
}

sealed class UserDetailsState {
    data object Loading : UserDetailsState()
    data class Success(val user: User) : UserDetailsState()
    data class Error(val message: String) : UserDetailsState()
}