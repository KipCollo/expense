package com.example.expense.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.data.model.Expense
import com.example.expense.repository.ExpenseRepository
import java.util.Date
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _expenses = mutableStateOf<List<Expense>>(emptyList())
    val expenses: State<List<Expense>> = _expenses

    init {
        viewModelScope.launch {
            repository.allExpenses.collectLatest { list ->
                _expenses.value = list
            }
        }
    }

    fun addExpense(title: String, amountText: String, location: String): Boolean {
        val amount = amountText.toDoubleOrNull() ?: return false
        if (title.isBlank()) return false
        if (amount <= 0.0) return false

        viewModelScope.launch {
            repository.insert(
                Expense(
                    title = title.trim(),
                    amount = amount,
                    date = Date(),
                    location = location.trim()
                )
            )
        }
        return true
    }
}
