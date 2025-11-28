package com.example.workmatetest.presentation

import com.example.workmatetest.domain.model.User

data class UsersState (
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null,
    val generationParams: GenerationParams = GenerationParams()
)

data class GenerationParams(
    val gender: String? = null,
    val nationality: String? = null
)