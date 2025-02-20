package com.example.userslistapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.userslistapp.data.local.dao.UsersDao
import com.example.userslistapp.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao

    companion object {
        const val db_name = "users_db"
    }
}