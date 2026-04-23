package com.example.expense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expense.service.UserService

class UserViewModelFactory(private val userService: UserService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}