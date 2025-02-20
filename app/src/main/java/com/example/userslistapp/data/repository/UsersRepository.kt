package com.example.userslistapp.data.repository

import android.util.Log
import com.example.userslistapp.data.local.dao.UsersDao
import com.example.userslistapp.data.local.entity.UserEntity
import com.example.userslistapp.data.mapper.toUserEntities
import com.example.userslistapp.data.network.api.UsersApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed class UsersState {
    data class Success(val data: List<UserEntity>) : UsersState()
    data class Error(val message: String) : UsersState()
    data object Loading : UsersState()
}

class UsersRepository @Inject constructor(
    private val api: UsersApi,
    private val dao: UsersDao
) {
    suspend fun getUsers(): Flow<UsersState> = flow {
        emit(UsersState.Loading)

        try {
            val response = api.getUsers()

            if (response.isEmpty()) {
                emit(UsersState.Error("No users found"))
                return@flow
            }

            val entities = response.toUserEntities()
            Log.d("UsersRes", entities.toString())
            dao.insertAll(entities)

            emit(UsersState.Success(entities))

        } catch (e: Exception) {
            val cached = dao.getUsers()
            Log.d("UsersRes", "caches: $cached")
            if (cached.isNotEmpty()) {
                emit(UsersState.Success(cached))
            } else {
                val errorMsg = when (e) {
                    is IOException -> "No internet connection"
                    is HttpException -> "Server error: ${e.code()}"
                    else -> "Failed to load data"
                }
                Log.d("UsersRes", errorMsg)
                emit(UsersState.Error(errorMsg))
            }
        }
    }
}