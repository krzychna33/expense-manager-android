package dev.krzychna33.expensemanager.data.datasource

import dev.krzychna33.expensemanager.data.entity.Expense

interface ExpensesDataSource {
    suspend fun getExpenses(): List<Expense>
    suspend fun addExpense(expense: Expense): String // Returns the ID of the new expense
}