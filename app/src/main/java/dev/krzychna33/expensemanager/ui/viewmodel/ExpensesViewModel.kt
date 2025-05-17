package dev.krzychna33.expensemanager.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krzychna33.expensemanager.data.entity.Expense
import dev.krzychna33.expensemanager.ui.repository.AuthRepository
import dev.krzychna33.expensemanager.ui.repository.ExpensesRepository
import dev.krzychna33.expensemanager.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject() constructor(
    private val expensesRepository: ExpensesRepository,
    private val authRepository: AuthRepository
) :
    ViewModel() {

    private val _expenses: MutableStateFlow<ResourceState<List<Expense>>> =
        MutableStateFlow(ResourceState.Loading())
    val expenses: StateFlow<ResourceState<List<Expense>>> = _expenses

    private val _addExpenseResult = MutableStateFlow<ResourceState<String>>(ResourceState.Loading())
    val addExpenseResult: StateFlow<ResourceState<String>> = _addExpenseResult


    init {
        Log.d("NewsViewModel", "Inside NewsViewModel init")
        getExpenses()
    }

    private fun getExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = authRepository.getCurrentUser()?.uid
                ?: throw IllegalStateException("User ID is null")

            expensesRepository.getExpenses(userId).collectLatest {
                Log.d("ExpensesViewModel", "Inside getExpenses collectLatest")
                _expenses.value = it
            }
        }
    }

    private fun validateExpense(
        name: String,
        amount: Double,
        category: String
    ): Boolean {
        return when {
            name.isBlank() -> {
                _addExpenseResult.value = ResourceState.Error("Name cannot be empty")
                false
            }
            amount <= 0 -> {
                _addExpenseResult.value = ResourceState.Error("Amount must be greater than 0")
                false
            }
            category.isBlank() -> {
                _addExpenseResult.value = ResourceState.Error("Category cannot be empty")
                false
            }
            else -> true
        }
    }

    fun addExpense(name: String, amount: Double, category: String) {
        if (!validateExpense(name, amount, category)) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val currentDateTime = dateFormat.format(Date())

            val userId = authRepository.getCurrentUser()?.uid
                ?: throw IllegalStateException("User ID is null")

            val newExpense = Expense(
                id = "",
                userId,
                name = name,
                amount = amount,
                category = category,
                date = currentDateTime
            )

            expensesRepository.addExpense(newExpense).collectLatest { result ->
                _addExpenseResult.value = result
                if (result is ResourceState.Success) {
                    getExpenses()
                }
            }
        }
    }
}