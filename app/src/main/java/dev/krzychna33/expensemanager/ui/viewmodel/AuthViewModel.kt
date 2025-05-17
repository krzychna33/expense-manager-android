package dev.krzychna33.expensemanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krzychna33.expensemanager.ui.repository.AuthRepository
import dev.krzychna33.expensemanager.utils.ResourceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _loginState = MutableStateFlow<ResourceState<Boolean>>(ResourceState.Idle())
    val loginState: StateFlow<ResourceState<Boolean>> = _loginState

    val isLoggedIn = authRepository.isLoggedIn

    fun login(email: String, password: String) {
        _loginState.value = ResourceState.Loading()
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            result.fold(
                onSuccess = { _loginState.value = ResourceState.Success(it) },
                onFailure = { _loginState.value = ResourceState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}