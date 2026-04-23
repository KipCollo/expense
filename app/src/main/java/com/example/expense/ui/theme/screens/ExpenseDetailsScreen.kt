package com.example.expense.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expense.data.model.Expense
import java.text.SimpleDateFormat

@Composable
fun ExpenseDetailsScreen (expense: Expense, onBack: () -> Unit) {

    Column(modifier = Modifier
    .fillMaxSize()
    .padding(16.dp)) {

        Text("Expense Details", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Title: ${expense.title}")
        Text("Amount: KES ${expense.amount}")
        Text("Date: ${SimpleDateFormat("dd MMM yyyy").format(expense.date)}")
        Text("Location: ${expense.location}")

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}