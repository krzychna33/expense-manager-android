package dev.krzychna33.expensemanager.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krzychna33.expensemanager.data.entity.Expense
import dev.krzychna33.expensemanager.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject() constructor()  : ViewModel() {

    private val _expenses: MutableStateFlow<ResourceState<List<Expense>>> =
        MutableStateFlow(ResourceState.Loading())
    val expenses: StateFlow<ResourceState<List<Expense>>> = _expenses


    init {
        Log.d("NewsViewModel", "Inside NewsViewModel init")
        getExpenses()
    }

    private fun getExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            // Simulate a network call

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val currentDateTime = dateFormat.format(Date())

            val expenses = listOf(
                Expense(
                    id = 1,
                    amount = 100.0,
                    category = "Groceries",
                    name = "Shopping",
                    date = currentDateTime
                ),
                Expense(
                    id = 2,
                    amount = 50.0,
                    category = "Transport",
                    name = "Tram ticket",
                    date = currentDateTime
                ),
                Expense(
                    id = 3,
                    amount = 200.0,
                    category = "Rent",
                    name = "rent",
                    date = currentDateTime
                )
            )
            _expenses.value = ResourceState.Success(expenses)
        }
    }
}