package dev.krzychna33.expensemanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.krzychna33.expensemanager.data.entity.Expense
import dev.krzychna33.expensemanager.ui.viewmodel.ExpensesViewModel
import dev.krzychna33.expensemanager.utils.ResourceState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    expensesViewModel: ExpensesViewModel = hiltViewModel(),
    onExpenseAdded: () -> Unit,
    onCancel: () -> Unit
) {
    val addExpenseState by expensesViewModel.addExpenseResult.collectAsState()
    val expensesState by expensesViewModel.expenses.collectAsState()

    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var expenseCategory by remember { mutableStateOf("Default") }
    var expanded by remember { mutableStateOf(false) }

    // Call onExpenseAdded only when addExpenseState is ResourceState.Success
    LaunchedEffect(addExpenseState) {
        if (addExpenseState is ResourceState.Success) {
            onExpenseAdded()
            expensesViewModel.resetAddExpenseResult()
        }
    }

    val categories = expensesState.let {
        if (it is ResourceState.Success) {
            it.data.map { expense -> expense.category }.distinct() + listOf("Default")
        } else {
            listOf("Default")
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text("Expense Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = expenseAmount,
            onValueChange = { newValue ->
                val pattern = Regex("^\\d*(\\.\\d{0,2})?")
                if (newValue.isEmpty() || pattern.matches(newValue)) {
                    expenseAmount = newValue
                }
            },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = expenseCategory,
                onValueChange = { expenseCategory = it },
                label = { Text("Category") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            expenseCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val amount = expenseAmount.toDoubleOrNull() ?: 0.0
                expensesViewModel.addExpense(expenseName, amount, expenseCategory)
                expenseName = ""
                expenseAmount = ""
                expenseCategory = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Expense")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onCancel() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
        Spacer(modifier = Modifier.height(8.dp))
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
}

