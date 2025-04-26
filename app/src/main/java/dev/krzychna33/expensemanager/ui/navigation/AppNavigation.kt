package dev.krzychna33.news2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.krzychna33.expensemanager.ui.screens.HomeScreen
import dev.krzychna33.expensemanager.ui.screens.LoginScreen
import dev.krzychna33.expensemanager.ui.viewmodel.AuthViewModel


@Composable
fun AppNavigationGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
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
                logout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN_SCREEN) {
                        popUpTo(Routes.HOME_SCREEN) { inclusive = true }
                    }
                }
            )
        }
    }
}