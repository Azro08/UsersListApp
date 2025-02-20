package com.example.userslistapp.di

import android.content.Context
import androidx.room.Room
import com.example.userslistapp.data.local.UsersDatabase
import com.example.userslistapp.data.local.dao.UsersDao
import com.example.userslistapp.data.network.api.UsersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    @Provides
    fun provideRetrofitInstance(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    fun provideUserApi(retrofit: Retrofit): UsersApi =
        retrofit.create(UsersApi::class.java)


    @Provides
    fun provideUsersDatabase(@ApplicationContext context: Context): UsersDatabase =
        Room.databaseBuilder(
            context,
            UsersDatabase::class.java,
            "users.db"
        ).build()

    @Provides
    fun provideUsersDao(database: UsersDatabase): UsersDao =
        database.usersDao()

}