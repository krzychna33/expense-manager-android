package dev.krzychna33.expensemanager.ui.repository

import android.util.Log
import dev.krzychna33.expensemanager.data.datasource.ExpensesDataSource
import dev.krzychna33.expensemanager.data.entity.Expense
import dev.krzychna33.expensemanager.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

const val TAG = "ExpensesRepository"

class ExpensesRepository @Inject constructor(private val expensesDataSource: ExpensesDataSource) {

    suspend fun getExpenses(userId: String): Flow<ResourceState<List<Expense>>> {
        Log.d(TAG, "Inside getExpenses")
        return flow {
            emit(ResourceState.Loading())
            val response = expensesDataSource.getExpenses(userId)
            emit(ResourceState.Success(response))
        }.catch { e ->
            Log.d(TAG, "Error: ${e.message}")
            emit(ResourceState.Error("Unknown error"))
        }
    }

    suspend fun addExpense(expense: Expense): Flow<ResourceState<String>> = flow {
        emit(ResourceState.Loading())
        try {
            val expenseId = expensesDataSource.addExpense(expense)
            emit(ResourceState.Success(expenseId))
        } catch (e: Exception) {
            emit(ResourceState.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun removeExpense(expense: Expense): Flow<ResourceState<String>> = flow {
        emit(ResourceState.Loading())
        try {
            val result = expensesDataSource.removeExpense(expense)
            emit(ResourceState.Success(result))
        } catch (e: Exception) {
            emit(ResourceState.Error(e.message ?: "Unknown error occurred"))
        }
    }
}

