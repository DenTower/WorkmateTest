package com.example.workmatetest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workmatetest.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val user: User,
)
