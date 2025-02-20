package com.example.userslistapp.data.mapper

import com.example.userslistapp.data.local.entity.UserEntity
import com.example.userslistapp.data.network.dto.UserResponseDto

fun UserResponseDto.toUserEntities(): List<UserEntity> {
    return this.map { dtoItem ->
        UserEntity(
            id = dtoItem.id,
            name = dtoItem.name,
            email = dtoItem.email,
            city = dtoItem.address.city,  // Extract nested city from address
            phone = dtoItem.phone
        )
    }
}