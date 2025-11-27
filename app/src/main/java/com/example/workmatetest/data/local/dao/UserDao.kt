package com.example.workmatetest.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.workmatetest.data.local.entities.UserEntity

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: UserEntity)

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<UserEntity>

}