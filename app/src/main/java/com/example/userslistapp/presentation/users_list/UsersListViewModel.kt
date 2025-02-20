package com.example.userslistapp.presentation.users_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userslistapp.data.repository.UsersRepository
import com.example.userslistapp.data.repository.UsersState
import com.example.userslistapp.presentation.mapper.toUser
import com.example.userslistapp.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
    private val repository: UsersRepository
) : ViewModel() {

    private val responseChannel = Channel<UsersUiState>(Channel.BUFFERED)
    val responseStates: Flow<UsersUiState> = responseChannel.receiveAsFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() = viewModelScope.launch {
        repository.getUsers().collect { state ->
            when (state) {
                is UsersState.Loading -> {
                    responseChannel.send(UsersUiState.Loading)
                }

                is UsersState.Success -> {
                    val users = state.data.map { it.toUser() }
                    responseChannel.send(UsersUiState.Success(users))
                }

                is UsersState.Error -> {
                    responseChannel.send(UsersUiState.Error(state.message))
                }
            }
        }
    }

    fun refreshData(){
        loadUsers()
    }
}

sealed class UsersUiState {
    data class Success(val data: List<User>) : UsersUiState()
    data class Error(val message: String) : UsersUiState()
    data object Loading : UsersUiState()
}