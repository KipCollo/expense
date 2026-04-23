package com.example.expense.ui.theme.screens

import androidx.compose.runtime.Composable
import com.example.expense.data.model.Expense

@Composable
fun ExpenseHistoryScreen(
    expenses: List<Expense>,
    onExpenseClick: (Int) -> Unit
) {
    HomeScreen(
        expenses = expenses,
        onAddClick = {},
        onInsightsClick = {},
        onExpenseClick = onExpenseClick
    )
}
