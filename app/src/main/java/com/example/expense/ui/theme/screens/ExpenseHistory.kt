package com.example.expense.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpenseHistoryScreen() {
    val expenses = listOf(
        Triple("Groceries", "USD 120", "2025-05-10"),
        Triple("Electricity Bill", "USD 300", "2025-05-07"),
        Triple("Internet", "USD 250", "2025-05-06"),
        Triple("Transport", "USD 400", "2025-05-05"),
        Triple("Subscription", "USD 100", "2025-05-03")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDE7))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Expense History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFA500))
                .padding(12.dp)
                .padding(bottom = 16.dp)
        )

        expenses.forEach { (title, amount, date) ->
            ExpenseItem(title = title, amount = amount, date = date)
        }
    }
}

@Composable
fun ExpenseItem(title: String, amount: String, date: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(Color(0xFFFFD54F), shape = RoundedCornerShape(4.dp))
            .padding(16.dp)
            .shadow(2.dp, RoundedCornerShape(4.dp))
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        Text(
            text = "$amount - $date",
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
