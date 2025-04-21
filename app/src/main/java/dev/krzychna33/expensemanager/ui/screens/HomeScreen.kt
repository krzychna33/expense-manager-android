package dev.krzychna33.expensemanager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.krzychna33.expensemanager.data.entity.Expense
import dev.krzychna33.expensemanager.ui.viewmodel.ExpensesViewModel
import dev.krzychna33.expensemanager.utils.ResourceState

const val TAG = "HomeScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun HomeScreen(expensesViewModel: ExpensesViewModel = hiltViewModel()) {
    val expensesState by expensesViewModel.expenses.collectAsState()
    val addExpenseState by expensesViewModel.addExpenseResult.collectAsState()

    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var expenseCategory by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Make the content area scrollable
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            when (expensesState) {
                is ResourceState.Loading -> {
                    Text("Loading expenses...")
                }
                is ResourceState.Success -> {
                    Text(text = "Expenses loaded successfully")
                    val expenses = (expensesState as ResourceState.Success<List<Expense>>).data

                    // Display each expense directly in the scrollable column
                    expenses.forEachIndexed { index, expense ->
                        ExpenseItem(expense = expense, index = index)
                    }

                    // Add the TotalExpensesSummary component
                }
                is ResourceState.Error -> {
                    Text(text = "Error loading expenses")
                }
                else -> {}
            }

            // Show add expense result feedback
            when (addExpenseState) {
                is ResourceState.Success -> {
                    Text(text = "Expense added successfully!")
                }
                is ResourceState.Error -> {
                    Text(text = "Error: ${(addExpenseState as ResourceState.Error).error}")
                }
                else -> {}
            }
        }

        // Input fields and Add Expense button (fixed at bottom)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {

            if (expensesState is ResourceState.Success) {
                val expenses = (expensesState as ResourceState.Success<List<Expense>>).data
                TotalExpensesSummary(expenses = expenses)
            }

            OutlinedTextField(
                value = expenseName,
                onValueChange = { expenseName = it },
                label = { Text("Expense Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = expenseAmount,
                onValueChange = { expenseAmount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = expenseCategory,
                onValueChange = { expenseCategory = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val amount = expenseAmount.toDoubleOrNull() ?: 0.0
                    expensesViewModel.addExpense(expenseName, amount, expenseCategory)
                    // Clear fields after submission
                    expenseName = ""
                    expenseAmount = ""
                    expenseCategory = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Expense")
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense, index: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "${index + 1}. ${expense.name}",
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Amount: $${expense.amount}",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Category: ${expense.category}",
            style = androidx.compose.material3.MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun TotalExpensesSummary(expenses: List<Expense>) {
    val totalAmount = expenses.sumOf { it.amount }
    Text(
        text = "Total Expenses: $${"%.2f".format(totalAmount)}",
        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 16.dp)
    )
}