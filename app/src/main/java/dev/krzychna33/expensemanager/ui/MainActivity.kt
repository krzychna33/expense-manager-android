package dev.krzychna33.expensemanager.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import dev.krzychna33.news2.ui.navigation.AppNavigationGraph
import dev.krzychna33.news2.ui.theme.ExpenseManagerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    init {
        Log.d("MainActivity", "Inside MainActivity init")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            ExpenseManagerTheme  {
                Surface(
                    modifier = Modifier.fillMaxSize().background(Color.Cyan)
                ) {
                    AppEntryPoint()
                }
            }
        }
    }
}

@Composable
fun AppEntryPoint() {
    AppNavigationGraph()
}


