package com.example.expense

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expense.data.model.Expense
import com.example.expense.ui.theme.screens.ExpenseCard
import java.util.Date

@Composable
fun ExpenseApp (modifier: Modifier = Modifier){

    val navController = rememberNavController()
    val expenses = remember { mutableStateListOf<Expense>() }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                expenses = expenses,
                onAddClick = { navController.navigate(Screen.AddExpense.route) },
                onExpenseClick = { id ->
                    navController.navigate(Screen.ExpenseDetail.createRoute(id))
                }
            )
        }
        composable(Screen.AddExpense.route) {
            AddExpenseScreen(
                onSave = { expense ->
                    expenses.add(expense)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable(Screen.ExpenseDetail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("expenseId")?.toIntOrNull()
            val expense = expenses.find { it.id == id }
            expense?.let {
                ExpenseDetailScreen(expense = it, onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun AddExpenseScreen(onSave: Any, onCancel: () -> Boolean) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val expense = Expense(
                    title = title,
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    date = Date(),
                    location = location
                )
                onSave(expense)
            }, modifier = Modifier.weight(1f)) {
                Text("Save")
            }
        }
    }
}

@Composable
fun HomeScreen(expenses: SnapshotStateList<Expense>, onAddClick: () -> Unit, onExpenseClick: Any) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) {
        LazyColumn(contentPadding = it) {
            items(expenses) { expense ->
                ExpenseCard(expense)
            }
        }
    }
}


