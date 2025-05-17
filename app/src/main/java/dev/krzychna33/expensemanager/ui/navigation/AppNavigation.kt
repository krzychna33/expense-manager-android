package dev.krzychna33.expensemanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.krzychna33.expensemanager.ui.screens.AddExpenseScreen
import dev.krzychna33.expensemanager.ui.screens.HomeScreen
import dev.krzychna33.expensemanager.ui.screens.LoginScreen
import dev.krzychna33.expensemanager.ui.viewmodel.AuthViewModel
import dev.krzychna33.expensemanager.ui.viewmodel.ExpensesViewModel

object Routes {
    const val LOGIN_SCREEN = "login"
    const val HOME_SCREEN = "home"
    const val ADD_EXPENSE_SCREEN = "add_expense"
}

@Composable
fun AppNavigationGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val expensesViewModel: ExpensesViewModel = hiltViewModel() // Shared instance
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState(initial = false)

    val startDestination = if (isLoggedIn) Routes.HOME_SCREEN else Routes.LOGIN_SCREEN

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(Routes.HOME_SCREEN) {
                        popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME_SCREEN) {
            HomeScreen(
                expensesViewModel = expensesViewModel,
                logout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN_SCREEN) {
                        popUpTo(Routes.HOME_SCREEN) { inclusive = true }
                    }
                },
                onAddExpense = {
                    navController.navigate(Routes.ADD_EXPENSE_SCREEN)
                }
            )
        }

        composable(Routes.ADD_EXPENSE_SCREEN) {
            AddExpenseScreen(
                expensesViewModel = expensesViewModel,
                onExpenseAdded = {
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}

