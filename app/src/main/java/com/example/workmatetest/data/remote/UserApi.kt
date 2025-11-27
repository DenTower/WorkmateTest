package com.example.workmatetest.data.remote

import com.example.workmatetest.data.remote.dto.UserDto

interface UserApi {

    suspend fun getNewUser(gender: String?, nationality: String?): UserDto

    suspend fun getUserImage(url: String): ByteArray

    companion object {
        const val BASE_URL = "https://randomuser.me/api/"
    }
}