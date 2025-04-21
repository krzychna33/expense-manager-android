package dev.krzychna33.news2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.krzychna33.expensemanager.ui.screens.HomeScreen

@Composable
fun AppNavigationGraph() {

    val navController = rememberNavController()

    NavHost(navController=navController, startDestination = Routes.HOME_SCREEN) {
        composable(Routes.HOME_SCREEN){
            HomeScreen()
        }
    }
}