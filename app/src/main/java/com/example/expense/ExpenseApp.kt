package com.example.expense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expense.data.database.AppDatabase
import com.example.expense.service.ExpenseService
import com.example.expense.service.UserService
import com.example.expense.ui.theme.screens.AddExpenseScreen
import com.example.expense.ui.theme.screens.ExpenseDetailsScreen
import com.example.expense.ui.theme.screens.ExpenseHistoryScreen
import com.example.expense.ui.theme.screens.HomeScreen
import com.example.expense.ui.theme.screens.InsightScreen
import com.example.expense.ui.theme.screens.LoginScreen
import com.example.expense.ui.theme.screens.RegisterScreen
import com.example.expense.ui.theme.screens.WelcomeScreen
import com.example.expense.viewmodel.ExpenseViewModel
import com.example.expense.viewmodel.ExpenseViewModelFactory
import com.example.expense.viewmodel.UserViewModel
import com.example.expense.viewmodel.UserViewModelFactory

private object Destinations {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val ADD = "add"
    const val INSIGHTS = "insights"
    const val EXPENSES = "expenses"
    const val DETAILS = "details/{expenseId}"
    const val DETAILS_PREFIX = "details"
}

@Composable
fun ExpenseApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = remember(context) { AppDatabase.getDatabase(context) }

    val expenseService = remember(database) { ExpenseService(database.expenseDao()) }
    val userService = remember(database) { UserService(database.userDao()) }

    val expenseViewModel: ExpenseViewModel = viewModel(
        factory = ExpenseViewModelFactory(expenseService)
    )
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userService)
    )

    val expenses by expenseViewModel.expenses

    NavHost(
        navController = navController,
        startDestination = Destinations.WELCOME,
        modifier = modifier
    ) {
        composable(Destinations.WELCOME) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Destinations.LOGIN) },
                onRegisterClick = { navController.navigate(Destinations.REGISTER) }
            )
        }

        composable(Destinations.LOGIN) {
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.WELCOME) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Destinations.REGISTER) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.REGISTER) {
            RegisterScreen(
                userViewModel = userViewModel,
                onRegisterSuccess = {
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(Destinations.REGISTER) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(Destinations.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.HOME) {
            HomeScreen(
                expenses = expenses,
                onAddClick = { navController.navigate(Destinations.ADD) },
                onInsightsClick = { navController.navigate(Destinations.INSIGHTS) },
                onExpenseClick = { id -> navController.navigate("${Destinations.DETAILS_PREFIX}/$id") },
                onViewAllClick = { navController.navigate(Destinations.EXPENSES) },
                onHistoryClick = { navController.navigate(Destinations.EXPENSES) }
            )
        }

        composable(Destinations.ADD) {
            AddExpenseScreen(
                onSaveClick = { title, amount, location, isIncome ->
                    val isSaved = expenseViewModel.addExpense(title, amount, location, isIncome)
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
                onBackClick = { navController.popBackStack() },
                onAddClick = { navController.navigate(Destinations.ADD) }
            )
        }

        composable(Destinations.EXPENSES) {
            ExpenseHistoryScreen(
                expenses = expenses,
                onExpenseClick = { id -> navController.navigate("${Destinations.DETAILS_PREFIX}/$id") },
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

