package com.example.expense.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expense.data.model.Expense

@Composable
fun InsightScreen(
    expenses: List<Expense>,
    onBackClick: () -> Unit
) {
    val total = expenses.sumOf { it.amount }
    val average = if (expenses.isNotEmpty()) total / expenses.size else 0.0
    val topLocation = expenses
        .groupBy { it.location.ifBlank { "Uncategorized" } }
        .maxByOrNull { (_, values) -> values.sumOf { it.amount } }
        ?.key
        ?: "N/A"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Insights", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        InsightCard(title = "Total Spend", value = total)
        InsightCard(title = "Average Expense", value = average)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Top Category/Location", style = MaterialTheme.typography.titleMedium)
                Text(topLocation, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}

@Composable
private fun InsightCard(title: String, value: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(String.format("%.2f", value), style = MaterialTheme.typography.bodyLarge)
        }
    }
}
