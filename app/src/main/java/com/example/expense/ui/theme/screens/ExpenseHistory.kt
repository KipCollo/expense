package com.example.expense.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expense.data.model.Expense
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseHistoryScreen(
    expenses: List<Expense>,
    onExpenseClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit = {},
    onInsightClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense History") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = NavRoute.Expense,
                onHomeClick = onHomeClick,
                onExpenseClick = {},
                onInsightClick = onInsightClick
            )
        }
    ) { paddingValues ->
        if (expenses.isEmpty()) {
            Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                Text(
                    text = "No expenses recorded yet.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                item {
                    Text(
                        text = "${expenses.size} expense(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                items(expenses, key = { it.id }) { expense ->
                    HistoryExpenseCard(
                        expense = expense,
                        onClick = { onExpenseClick(expense.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryExpenseCard(expense: Expense, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(expense.title, style = MaterialTheme.typography.titleMedium)
        Text(
            text = "Amount: ${String.format("%.2f", expense.amount)}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Date: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(expense.date)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (expense.location.isNotBlank()) {
            Text(
                text = "Category: ${expense.location}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
