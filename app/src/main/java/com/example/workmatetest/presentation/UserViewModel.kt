package com.example.workmatetest.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmatetest.data.UserRepository
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

    fun generateNewUser(gender: String? = null, nationality: String? = null) {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)

            val result = repository.getNewUser(gender, nationality)

            result.onSuccess {
                _state.value = state.value.copy(isLoading = false, users = state.value.users + it)
            }.onFailure {
                _state.value = UsersState(error = "Unable to generate: ${ it.message ?: "Unknown error" }")
                Log.d("MyLog", "${it.message}")
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val users = repository.loadUsers()
            _state.value = state.value.copy(isLoading = false, users = users)
        }
    }
}