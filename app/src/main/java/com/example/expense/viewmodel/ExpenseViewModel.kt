package com.example.expense.viewmodel

import com.example.expense.data.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExpenseViewModel {
    // Internal mutable state
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())

    // External immutable state
    val expenses: StateFlow<List<Expense>> = _expenses

    // Function to add a new expense
    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            _expenses.value = _expenses.value + expense
        }
    }

    // Function to remove an expense by ID
    fun removeExpense(id: Int) {
        viewModelScope.launch {
            _expenses.value = _expenses.value.filter { it.id != id }
        }
    }
}