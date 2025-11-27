package com.example.workmatetest.data

import android.content.Context
import com.example.workmatetest.data.local.dao.UserDao
import com.example.workmatetest.data.local.entities.UserEntity
import com.example.workmatetest.data.remote.UserApi
import com.example.workmatetest.domain.model.User
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.util.UUID

class UserRepositoryImpl(
    private val api: UserApi,
    private val context: Context,
    private val userDao: UserDao,
): UserRepository {

    override suspend fun getNewUser(gender: String?, nationality: String?): Result<User> {

        return runCatching {
            val userDto = api.getNewUser(gender, nationality)
            val pictureURL = userDto.results.first().picture.large
            val picture = api.getUserImage(url = pictureURL)
            val filePath = saveUserPicture(picture)
            val userEntity = UserEntity(
                user = userDto.toUser(filePath)
            )
            userDao.upsertUser(userEntity)

            // return
            userDto.toUser(filePath)
        }
    }

    private fun saveUserPicture(bytes: ByteArray): String {
        val dir = File(context.filesDir, "users_pictures")
        if(!dir.exists()) dir.mkdirs()

        val fileName = "${UUID.randomUUID()}.jpg"
        val file = File(dir, fileName)

        file.outputStream().use { it.write(bytes) }

        return file.absolutePath
    }

    override suspend fun loadUsers(): List<User> {
        return userDao.getUsers().map { it.user }
    }
}
