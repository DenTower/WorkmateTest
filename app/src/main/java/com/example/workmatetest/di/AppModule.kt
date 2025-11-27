package com.example.workmatetest.di

import android.content.Context
import com.example.workmatetest.data.UserRepository
import com.example.workmatetest.data.UserRepositoryImpl
import com.example.workmatetest.data.local.dao.UserDao
import com.example.workmatetest.data.remote.UserApi
import com.example.workmatetest.data.remote.UserApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
    }

    @Provides
    @Singleton
    fun provideUsersApi(client: HttpClient): UserApi {
        return UserApiImpl(client)
    }

    @Provides
    @Singleton
    fun provideUsersRepository(
        @ApplicationContext context: Context,
        api: UserApi,
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(api, context, userDao)
    }
}