package dev.krzychna33.expensemanager.ui.screens

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import com.google.firebase.auth.FirebaseUser
import dev.krzychna33.expensemanager.data.entity.Expense
import dev.krzychna33.expensemanager.ui.components.ExpenseItem
import dev.krzychna33.expensemanager.ui.components.FilterChipsRow
import dev.krzychna33.expensemanager.ui.components.TotalExpensesSummary
import dev.krzychna33.expensemanager.ui.viewmodel.AuthViewModel
import dev.krzychna33.expensemanager.ui.viewmodel.ExpensesViewModel
import dev.krzychna33.expensemanager.utils.ResourceState

const val TAG = "HomeScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun HomeScreen(
    expensesViewModel: ExpensesViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    logout: () -> Unit,
    onAddExpense: () -> Unit,
) {

    val expensesState by expensesViewModel.expenses.collectAsState()
    val distinctCategoriesState by expensesViewModel.distinctCategories.collectAsState()
    val currentUserState by authViewModel.currentUser.collectAsState()
    val fabMenuExpanded = remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Scaffold(
        floatingActionButton = {
            if (expensesState !is ResourceState.Loading) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    FloatingActionButton(onClick = { fabMenuExpanded.value = !fabMenuExpanded.value }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = fabMenuExpanded.value,
                        onDismissRequest = { fabMenuExpanded.value = false }) {
                        DropdownMenuItem(text = { Text("Add Expense") }, onClick = {
                            fabMenuExpanded.value = false
                            onAddExpense()
                        }, leadingIcon = { Icon(Icons.Filled.Add, contentDescription = null) })
                        DropdownMenuItem(text = { Text("Logout") }, onClick = {
                            fabMenuExpanded.value = false
                            logout()
                            expensesViewModel.cleanUp()
                        }, leadingIcon = { Icon(Icons.Filled.ExitToApp, contentDescription = null) })
                    }
                }
            }
        }, floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        if (expensesState is ResourceState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 5.dp)
                ) {
                    Text("Hi ${(currentUserState)?.email}!")
                }
                if (distinctCategoriesState is ResourceState.Success) {
                    FilterChipsRow(
                        categories = (distinctCategoriesState as ResourceState.Success<List<String>>).data,
                        selectedCategory = selectedCategory,
                        onCategorySelected = {
                            selectedCategory = it
                        })
                }

                // Make the content area scrollable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    when (expensesState) {
                        is ResourceState.Success -> {
                            val expenses = (expensesState as ResourceState.Success<List<Expense>>).data
                            if (expenses.isEmpty()) {
                                Text(text = "You have no expenses yet.")
                            } else {
                                val filteredExpenses = selectedCategory?.let { cat ->
                                    expenses.filter { it.category == cat }
                                } ?: expenses

                                Text(text = "Your expenses (showing ${filteredExpenses.size} of ${expenses.size}):")
                                filteredExpenses.forEachIndexed { index, expense ->
                                    ExpenseItem(
                                        expense = expense,
                                        index = index,
                                        onRemove = { expensesViewModel.removeExpense(it) })
                                }
                            }

                        }

                        is ResourceState.Error -> {
                            Text(text = "Error loading expenses")
                        }

                        else -> {
                        }
                    }
                }
                // Summary only
                if (expensesState is ResourceState.Success) {
                    val expenses = (expensesState as ResourceState.Success<List<Expense>>).data
                    val filteredExpenses = selectedCategory?.let { cat ->
                        expenses.filter { it.category == cat }
                    } ?: expenses
                    TotalExpensesSummary(expenses = filteredExpenses)
                }
            }
        }
    }
}
