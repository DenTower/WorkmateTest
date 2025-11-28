package com.example.workmatetest.data

import android.content.Context
import com.example.workmatetest.data.local.dao.UserDao
import com.example.workmatetest.data.remote.UserApi
import com.example.workmatetest.domain.model.User
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
            val userEntity = userDto.toUserEntity(filePath)

            userDao.upsertUser(userEntity)

            // return
            userEntity.toUser()
        }

    }

    override suspend fun removeUser(user: User) {
        userDao.deleteUser(user.toUserEntity())
    }

    override suspend fun loadUsers(): List<User> {
        return userDao.getUsers().map { it.toUser() }
    }

    private fun saveUserPicture(bytes: ByteArray): String {
        val dir = File(context.filesDir, "users_pictures")
        if(!dir.exists()) dir.mkdirs()

        val fileName = "${UUID.randomUUID()}.jpg"
        val file = File(dir, fileName)

        file.outputStream().use { it.write(bytes) }

        return file.absolutePath
    }
}
