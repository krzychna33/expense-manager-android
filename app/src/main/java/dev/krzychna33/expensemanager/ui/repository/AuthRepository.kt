package dev.krzychna33.expensemanager.ui.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.krzychna33.expensemanager.data.datasource.AuthDataSource
import dev.krzychna33.expensemanager.data.local.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
) {

    suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            val result = authDataSource.login(email, password)
            result.fold(
                onSuccess = {
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
            val result = authDataSource.logout()
            result.fold(
                onSuccess = {
                    Result.success(true)
                },
                onFailure = { Result.failure(it) }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): FirebaseUser? {
        return authDataSource.getCurrentUser()
    }

    suspend fun signUp(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = authDataSource.signUp(email, password)
            result.fold(
                onSuccess = { Result.success(it) },
                onFailure = { Result.failure(it) }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}