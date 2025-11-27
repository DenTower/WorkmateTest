package com.example.workmatetest.presentation

import com.example.workmatetest.domain.model.User

data class UsersState (
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null,
)