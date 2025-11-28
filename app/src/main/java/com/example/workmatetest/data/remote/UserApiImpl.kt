package com.example.workmatetest.data.remote

import com.example.workmatetest.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.util.Locale.getDefault

class UserApiImpl(
    private val client: HttpClient
): UserApi {
    override suspend fun getNewUser(gender: String?, nationality: String?): UserDto {
        val response = client.get(UserApi.BASE_URL) {
            parameter("gender", gender?.lowercase(getDefault()))
            parameter("nat", nationality)
        }
        return response.body<UserDto>()
    }

    override suspend fun getUserImage(url: String): ByteArray {
        val response = client.get(url)
        return response.body()
    }
}