package dev.krzychna33.expensemanager.data.datasource

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun logout(): Result<Boolean>
    suspend fun isLoggedIn(): Flow<Boolean>
    suspend fun signUp(email: String, password: String): Result<FirebaseUser>
    suspend fun getCurrentUser(): FirebaseUser?
}