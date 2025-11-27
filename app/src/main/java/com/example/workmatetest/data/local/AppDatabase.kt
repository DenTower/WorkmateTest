package com.example.workmatetest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.workmatetest.data.local.converters.UserConverter
import com.example.workmatetest.data.local.dao.UserDao
import com.example.workmatetest.data.local.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1
)
@TypeConverters(UserConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}