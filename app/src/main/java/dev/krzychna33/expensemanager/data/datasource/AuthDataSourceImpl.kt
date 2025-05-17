package dev.krzychna33.expensemanager.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class AuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthDataSource {

    override suspend fun isLoggedIn(): Flow<Boolean>  =
        flow {
            emit(firebaseAuth.currentUser != null)
        }

    override suspend fun logout(): Result<Boolean> {
        return try {
            firebaseAuth.signOut()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<FirebaseUser> {
        delay(1000)
        if (email.isNotEmpty() && password.isNotEmpty()) {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                return Result.success(authResult.user!!)
            } else {
                return Result.failure(Exception("Login failed"))
            }
        } else {
            return Result.failure(Exception("Password or email is empty"))
        }
    }

}