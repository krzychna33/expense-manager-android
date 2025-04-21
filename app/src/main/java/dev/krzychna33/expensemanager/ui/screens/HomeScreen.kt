package dev.krzychna33.news2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dev.krzychna33.expensemanager.data.entity.Expense
import dev.krzychna33.expensemanager.ui.viewmodel.ExpensesViewModel
import dev.krzychna33.expensemanager.utils.ResourceState

const val TAG = "HomeScreen"

@Composable
fun HomeScreen( expensesViewModel: ExpensesViewModel = hiltViewModel()) {

    val expensesState by expensesViewModel.expenses.collectAsState()
    Column (modifier = Modifier.fillMaxWidth()) {
        when (expensesState) {
            is ResourceState.Loading -> {
                Text("Loading expenses...")
            }
            is ResourceState.Success -> {
                Text(text = "Expenses loaded successfully")
                val expenses = (expensesState as ResourceState.Success<List<Expense>>).data
                ExpensesList(expenses)
            }
            is ResourceState.Error -> {
                Text(text = "Error loading expenses")
            }
        }
    }
}

@Composable
fun ExpensesList(expenses: List<Expense>) {
    LazyColumn() {
        itemsIndexed(expenses) { index, expense: Expense ->
            Text("${index+1}: ${expense.name}")
        }
    }
}
