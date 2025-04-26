package dev.krzychna33.expensemanager.data.datasource

interface AuthDataSource {
    suspend fun login(email: String, password: String): Result<String> // Returns JWT token
}