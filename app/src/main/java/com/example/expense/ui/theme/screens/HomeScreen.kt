package com.example.expense.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expense.data.model.Expense
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeScreen(
    expenses: List<Expense>,
    onAddClick: () -> Unit,
    onInsightsClick: () -> Unit,
    onExpenseClick: (Int) -> Unit,
    onViewAllClick: () -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            item {
                Text(
                    text = "My Expenses",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "View insights",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable(onClick = onInsightsClick)
                )
                Text(
                    text = "View all expenses",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable(onClick = onViewAllClick)
                )
            }

            if (expenses.isEmpty()) {
                item {
                    Text(
                        text = "No expenses yet. Tap + to add one.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(expenses, key = { it.id }) { expense ->
                    ExpenseCard(
                        expense = expense,
                        onClick = { onExpenseClick(expense.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ExpenseCard(
    expense: Expense,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(expense.title, style = MaterialTheme.typography.titleMedium)
            Text("Amount: ${expense.amount}", style = MaterialTheme.typography.bodyLarge)
            Text(
                "Date: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(expense.date)}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (expense.location.isNotBlank()) {
                Text("Category: ${expense.location}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
