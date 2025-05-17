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
    private val firebaseAuth: FirebaseAuth
) {
    private val _authStateFlow = MutableStateFlow(firebaseAuth.currentUser != null)
    val isLoggedIn: Flow<Boolean> = _authStateFlow.asStateFlow()

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _authStateFlow.value = auth.currentUser != null
        }
    }

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
        return firebaseAuth.currentUser
    }
}