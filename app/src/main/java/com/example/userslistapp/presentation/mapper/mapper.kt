package com.example.userslistapp.presentation.mapper

import com.example.userslistapp.data.local.entity.UserEntity
import com.example.userslistapp.presentation.model.User

fun UserEntity.toUser() = User(
    id = id,
    name = name,
    email = email,
    city = city,
    phone = phone
)