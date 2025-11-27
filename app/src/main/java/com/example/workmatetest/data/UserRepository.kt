package com.example.workmatetest.data

import com.example.workmatetest.domain.model.User

interface UserRepository {
    suspend fun getNewUser(gender: String?, nationality: String?): Result<User>

    suspend fun loadUsers(): List<User>

}