package com.example.userslistapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("users")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val email: String,
    val city: String,
    val phone: String
)
