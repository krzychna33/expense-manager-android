package dev.krzychna33.expensemanager.data.datasource

import dev.krzychna33.expensemanager.data.entity.Expense

interface ExpensesDataSource {
    suspend fun getExpenses(userId: String): List<Expense>
    suspend fun addExpense(expense: Expense): String
    suspend fun removeExpense(expense: Expense): String
}

