package com.example.expense.ui.theme.screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expense.data.model.Expense
import com.example.expense.ui.theme.ChartColor1
import com.example.expense.ui.theme.ChartColor2
import com.example.expense.ui.theme.ChartColor3
import com.example.expense.ui.theme.ChartColor4
import com.example.expense.ui.theme.ChartColor5
import com.example.expense.ui.theme.ExpenseRed
import com.example.expense.ui.theme.IncomeGreen
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

private val pieChartColors = listOf(
    ChartColor1, ChartColor2, ChartColor3, ChartColor4, ChartColor5,
    Color(0xFF616161), Color(0xFF484848), Color(0xFF303030)
)

private enum class Period { Daily, Weekly, Monthly }

@Composable
fun InsightScreen(
    expenses: List<Expense>,
    onBackClick: () -> Unit,
    onExpenseClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    var selectedPeriod by remember { mutableStateOf(Period.Monthly) }

    val now = Calendar.getInstance()
    val filteredExpenses = expenses.filter { expense ->
        val cal = Calendar.getInstance().apply { time = expense.date }
        when (selectedPeriod) {
            Period.Daily -> cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                    cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)

            Period.Weekly -> cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                    cal.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR)

            Period.Monthly -> cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                    cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
        }
    }

    val totalIncome = filteredExpenses.filter { it.isIncome }.sumOf { it.amount }
    val totalExpense = filteredExpenses.filter { !it.isIncome }.sumOf { it.amount }

    val categoryTotals = filteredExpenses
        .filter { !it.isIncome }
        .groupBy { it.location.ifBlank { "Other" } }
        .mapValues { (_, list) -> list.sumOf { it.amount } }
        .entries
        .sortedByDescending { it.value }

    val goodPercent = ((totalIncome - totalExpense).coerceAtLeast(0.0) /
            totalIncome.coerceAtLeast(1.0) * 100).toInt().coerceIn(0, 100)

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = NavRoute.Insight,
                onHomeClick = onBackClick,
                onExpenseClick = onExpenseClick,
                onInsightClick = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Expenses",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                )
                IconButton(onClick = onAddClick) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }

            // Period tabs
            PeriodTabRow(
                selected = selectedPeriod,
                onSelect = { selectedPeriod = it },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Totals row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total Income",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = IncomeGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = currencyFormat.format(totalIncome),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = IncomeGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total Expense",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ExpenseRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = currencyFormat.format(totalExpense),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = ExpenseRed,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (categoryTotals.isNotEmpty()) {
                // Analytics header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onExpenseClick)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Analytics",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "View All",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pie chart
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    SpendingPieChart(
                        categoryTotals = categoryTotals,
                        total = totalExpense,
                        currencyFormat = currencyFormat,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Text(
                    text = "No expense data for this period.",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // "X% looks good" card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "$goodPercent% of your expenses looks good",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PeriodTabRow(
    selected: Period,
    onSelect: (Period) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        Row {
            Period.entries.forEach { period ->
                val isSelected = period == selected
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { onSelect(period) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = period.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SpendingPieChart(
    categoryTotals: List<Map.Entry<String, Double>>,
    total: Double,
    currencyFormat: NumberFormat,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            if (total <= 0.0) return@Canvas

            val diameter = size.minDimension * 0.65f
            val topLeft = Offset(
                x = (size.width - diameter) / 2f,
                y = (size.height - diameter) / 2f
            )
            val arcSize = Size(diameter, diameter)
            var startAngle = -90f

            categoryTotals.forEachIndexed { index, entry ->
                val sweep = (entry.value / total * 360f).toFloat()
                val color = pieChartColors[index % pieChartColors.size]

                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    topLeft = topLeft,
                    size = arcSize
                )

                // Percentage label inside slice
                val midAngle = Math.toRadians((startAngle + sweep / 2.0))
                val cx = size.width / 2f
                val cy = size.height / 2f
                val labelRadius = diameter * 0.3f
                val lx = cx + labelRadius * cos(midAngle).toFloat()
                val ly = cy + labelRadius * sin(midAngle).toFloat()

                drawContext.canvas.nativeCanvas.drawText(
                    "${(entry.value / total * 100).toInt()}%",
                    lx,
                    ly + 5f,
                    Paint().apply {
                        textAlign = Paint.Align.CENTER
                        textSize = 28f
                        this.color = Color.White.toArgb()
                        isFakeBoldText = true
                    }
                )

                startAngle += sweep
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        categoryTotals.forEachIndexed { index, entry ->
            val color = pieChartColors[index % pieChartColors.size]
            val percent = if (total > 0) (entry.value / total * 100).toInt() else 0
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = entry.key,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = currencyFormat.format(entry.value),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$percent%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
    }
}

