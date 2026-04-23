package com.example.expense.ui.theme.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.expense.data.model.Expense

private val chartColors = listOf(
    Color(0xFF6200EE),
    Color(0xFF03DAC6),
    Color(0xFFFF6D00),
    Color(0xFF00BCD4),
    Color(0xFFE91E63),
    Color(0xFF8BC34A),
    Color(0xFFFFB300),
    Color(0xFF607D8B)
)

@Composable
fun InsightScreen(
    expenses: List<Expense>,
    onBackClick: () -> Unit
) {
    val total = expenses.sumOf { it.amount }
    val average = if (expenses.isNotEmpty()) total / expenses.size else 0.0
    val categoryTotals = expenses
        .groupBy { it.location.ifBlank { "Uncategorized" } }
        .mapValues { (_, values) -> values.sumOf { it.amount } }
        .entries
        .sortedByDescending { it.value }
    val topCategory = categoryTotals.firstOrNull()?.key ?: "N/A"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Insights", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        InsightCard(title = "Total Spend", value = total)
        InsightCard(title = "Average Expense", value = average)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Top Category", style = MaterialTheme.typography.titleMedium)
                Text(topCategory, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (categoryTotals.isNotEmpty()) {
            Text("Spending by Category", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
            SpendingBarChart(categoryTotals = categoryTotals)
            Spacer(modifier = Modifier.height(16.dp))
            CategoryLegend(categoryTotals = categoryTotals, total = total)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}

@Composable
private fun SpendingBarChart(categoryTotals: List<Map.Entry<String, Double>>) {
    val maxValue = categoryTotals.maxOfOrNull { it.value } ?: 1.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            categoryTotals.forEachIndexed { index, entry ->
                val fraction = (entry.value / maxValue).toFloat().coerceIn(0f, 1f)
                val color = chartColors[index % chartColors.size]

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = entry.key,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(90.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(22.dp)
                    ) {
                        val barWidth = size.width * fraction
                        if (barWidth > 0f) {
                            drawRoundRect(
                                color = color,
                                topLeft = Offset(0f, 0f),
                                size = Size(barWidth, size.height),
                                cornerRadius = CornerRadius(6f, 6f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = String.format("%.0f", entry.value),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.width(56.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryLegend(categoryTotals: List<Map.Entry<String, Double>>, total: Double) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Breakdown", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            categoryTotals.forEachIndexed { index, entry ->
                val color = chartColors[index % chartColors.size]
                val percent = if (total > 0) (entry.value / total * 100) else 0.0
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(color = color)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = entry.key,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = String.format("%.1f%%", percent),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
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

