package dev.krzychna33.expensemanager.ui.repository

import dev.krzychna33.expensemanager.data.datasource.AuthDataSource
import dev.krzychna33.expensemanager.data.local.TokenManager
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val tokenManager: TokenManager
) {
    val isLoggedIn = tokenManager.isLoggedIn()

    suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            val result = authDataSource.login(email, password)
            result.fold(
                onSuccess = { token ->
                    tokenManager.saveToken(token)
                    Result.success(true)
                },
                onFailure = { Result.failure(it) }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Boolean> {
        return try {
            tokenManager.deleteToken()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}