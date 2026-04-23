package com.example.expense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.data.dao.UserDao
import com.example.expense.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel (private val userDao: UserDao) : ViewModel(){

    private val _loginResult = MutableStateFlow<Boolean?>(null)
    val loginResult: StateFlow<Boolean?> = _loginResult

    fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            val user = User(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password
            )
            userDao.register(user)
        }
    }


    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            val user = userDao.login(username, password)
            _loginResult.value = user != null
        }
    }
}