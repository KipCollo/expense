package com.example.expense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expense.data.database.AppDatabase
import com.example.expense.repository.ExpenseRepository
import com.example.expense.ui.theme.screens.AddExpenseScreen
import com.example.expense.ui.theme.screens.ExpenseDetailsScreen
import com.example.expense.ui.theme.screens.HomeScreen
import com.example.expense.ui.theme.screens.InsightScreen
import com.example.expense.viewmodel.ExpenseViewModel
import com.example.expense.viewmodel.ExpenseViewModelFactory

private object Destinations {
    const val HOME = "home"
    const val ADD = "add"
    const val INSIGHTS = "insights"
    const val DETAILS = "details/{expenseId}"
    const val DETAILS_PREFIX = "details"
}

@Composable
fun ExpenseApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = remember(context) { AppDatabase.getDatabase(context) }
    val repository = remember(database) { ExpenseRepository(database.expenseDao()) }
    val expenseViewModel: ExpenseViewModel = viewModel(
        factory = ExpenseViewModelFactory(repository)
    )

    val expenses by expenseViewModel.expenses

    NavHost(
        navController = navController,
        startDestination = Destinations.HOME,
        modifier = modifier
    ) {
        composable(Destinations.HOME) {
            HomeScreen(
                expenses = expenses,
                onAddClick = { navController.navigate(Destinations.ADD) },
                onInsightsClick = { navController.navigate(Destinations.INSIGHTS) },
                onExpenseClick = { id -> navController.navigate("${Destinations.DETAILS_PREFIX}/$id") }
            )
        }

        composable(Destinations.ADD) {
            AddExpenseScreen(
                onSaveClick = { title, amount, location ->
                    val isSaved = expenseViewModel.addExpense(title, amount, location)
                    if (isSaved) {
                        navController.popBackStack()
                    }
                    isSaved
                },
                onCancelClick = { navController.popBackStack() }
            )
        }

        composable(Destinations.INSIGHTS) {
            InsightScreen(
                expenses = expenses,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Destinations.DETAILS,
            arguments = listOf(navArgument("expenseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("expenseId") ?: return@composable
            val selectedExpense = expenses.firstOrNull { it.id == id } ?: return@composable
            ExpenseDetailsScreen(
                expense = selectedExpense,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
