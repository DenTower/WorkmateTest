package com.example.workmatetest.data.local.converters

import androidx.room.TypeConverter
import com.example.workmatetest.domain.model.User
import kotlinx.serialization.json.Json

class UserConverter {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun fromUser(user: User): String {
        return json.encodeToString(user)
    }

    @TypeConverter
    fun toUser(userString: String): User {
        return json.decodeFromString(userString)
    }
}
