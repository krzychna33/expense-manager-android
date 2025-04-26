package dev.krzychna33.expensemanager.data.local

import kotlinx.coroutines.flow.Flow

interface TokenManager {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun deleteToken()
    fun isLoggedIn(): Flow<Boolean>
}