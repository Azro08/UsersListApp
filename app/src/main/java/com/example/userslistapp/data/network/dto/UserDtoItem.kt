package com.example.userslistapp.data.network.dto


data class UserDtoItem(
    val address: Address,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
)