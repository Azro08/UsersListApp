package com.example.userslistapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.userslistapp.data.local.entity.UserEntity

@Dao
interface UsersDao {
    @Upsert
    suspend fun insertAll(usersList: List<UserEntity>)

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<UserEntity>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity
}