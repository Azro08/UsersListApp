package com.example.userslistapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.userslistapp.data.local.UsersDatabase
import com.example.userslistapp.data.local.dao.UsersDao
import com.example.userslistapp.data.local.entity.UserEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsersDaoTest {

    private lateinit var db: UsersDatabase
    private lateinit var usersDao: UsersDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, UsersDatabase::class.java).build()
        usersDao = db.usersDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testInsertAll() = runBlocking {
        val users = listOf(
            UserEntity(
                id = 1,
                name = "John",
                email = "jhon@gmail.com",
                city = "Paris",
                phone = "382-49-31"
            )
        )
        usersDao.insertAll(users)

        val result = usersDao.getUsers()

        assertThat(result.size, `is`(1))
        assertThat(result[0].name, `is`("John"))
        assertThat(result[0].city, `is`("Paris"))
    }

    @Test
    fun testGetUserById() = runBlocking {
        val user = UserEntity(
            id = 1,
            name = "John",
            email = "jhon@gmail.com",
            city = "Paris",
            phone = "382-49-31"
        )
        usersDao.insertAll(listOf(user))

        val result = usersDao.getUserById(1)

        assertThat(result.name, `is`("John"))
    }
}
