package com.example.expense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.data.model.User
import com.example.expense.service.UserService
import com.example.expense.util.PasswordUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userService: UserService) : ViewModel() {

    private val _loginResult = MutableStateFlow<Boolean?>(null)
    val loginResult: StateFlow<Boolean?> = _loginResult

    private val _registerResult = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerResult: StateFlow<RegisterState> = _registerResult

    fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = User(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = PasswordUtils.hash(password)
                )
                userService.register(user)
                _registerResult.value = RegisterState.Success
            } catch (e: Exception) {
                _registerResult.value = RegisterState.Error("Registration failed. Email may already be in use.")
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val user = userService.login(email, PasswordUtils.hash(password))
            _loginResult.value = user != null
        }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }

    fun resetRegisterResult() {
        _registerResult.value = RegisterState.Idle
    }
}

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}
