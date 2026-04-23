package com.example.expense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expense.service.ExpenseService

class ExpenseViewModelFactory(
    private val service: ExpenseService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
