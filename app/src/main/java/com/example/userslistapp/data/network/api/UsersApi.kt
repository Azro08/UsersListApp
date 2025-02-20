package com.example.userslistapp.data.network.api

import com.example.userslistapp.data.network.dto.UserResponseDto
import retrofit2.http.GET

interface UsersApi {

    @GET("users")
    suspend fun getUsers() : UserResponseDto

}