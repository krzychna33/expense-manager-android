package dev.krzychna33.expensemanager.data.datasource

import javax.inject.Inject
import kotlinx.coroutines.delay


class AuthDataSourceImpl @Inject constructor() : AuthDataSource {
    override suspend fun login(email: String, password: String): Result<String> {
        delay(1000)
        return if (email.isNotEmpty() && password.isNotEmpty() && password == "maslo") {
            Result.success("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxMjM0NTYiLCJlbWFpbCI6InVzZXJAZXhhbXBsZS5jb20ifQ.abc123signature")
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

}