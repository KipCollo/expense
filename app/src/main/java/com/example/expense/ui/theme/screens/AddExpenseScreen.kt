package com.example.expense.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddExpenseScreen(
    onSaveClick: (String, String, String) -> Boolean,
    onCancelClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var showValidationError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Add Expense", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                showValidationError = false
            },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                showValidationError = false
            },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Category/Location") },
            modifier = Modifier.fillMaxWidth()
        )

        if (showValidationError) {
            Text(
                text = "Enter a title and valid amount",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onCancelClick, modifier = Modifier.weight(1f)) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    val saved = onSaveClick(title, amount, location)
                    if (!saved) {
                        showValidationError = true
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }
        }
    }
}
