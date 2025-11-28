package com.example.workmatetest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmatetest.COUNTRIES
import com.example.workmatetest.data.UserRepository
import com.example.workmatetest.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    private val _state = MutableStateFlow(UsersState())
    val state: StateFlow<UsersState> = _state

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            val users = repository.loadUsers()
            _state.value = state.value.copy(users = users)
        }
    }

    fun generateNewUser(gender: String? = null, nationality: String? = null) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoading = true,
                generationParams = GenerationParams(gender, nationality)
            )

            val natCode = COUNTRIES.find { it.name == nationality }?.code
            val result = repository.getNewUser(gender, natCode)

            result.onSuccess {
                _state.value = state.value.copy(
                    isLoading = false,
                    users = state.value.users + it
                )
            }.onFailure {
                _state.value = state.value.copy(
                    isLoading = false,
                    error = "Unable to generate user: ${it.message ?: "Unknown error"}"
                )
            }
        }
    }

    fun retryGenerateNewUser() {
        val genParams = state.value.generationParams
        generateNewUser(gender = genParams.gender, nationality = genParams.nationality)
    }

    fun clearError() {
        _state.value = state.value.copy(error = null)
    }

    fun removeUser(user: User) {
        viewModelScope.launch {
            repository.removeUser(user)
            _state.value = state.value.copy(users = state.value.users - user)
        }
    }
}