package dev.krzychna33.expensemanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krzychna33.expensemanager.ui.repository.AuthRepository
import dev.krzychna33.expensemanager.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val _authStateFlow = MutableStateFlow(firebaseAuth.currentUser != null)
    val isLoggedIn: Flow<Boolean> = _authStateFlow.asStateFlow()

    private val _loginState = MutableStateFlow<ResourceState<Boolean>>(ResourceState.Idle())
    val loginState: StateFlow<ResourceState<Boolean>> = _loginState

    private val _signUpState = MutableStateFlow<ResourceState<FirebaseUser>>(ResourceState.Idle())
    val signUpState: StateFlow<ResourceState<FirebaseUser>> = _signUpState

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _authStateFlow.value = auth.currentUser != null
        }
        firebaseAuth.addAuthStateListener { auth ->
            if (auth.currentUser != null) {
                _currentUser.value = auth.currentUser
            }
        }
    }

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

    fun signUp(email: String, password: String) {
        _signUpState.value = ResourceState.Loading()
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            result.fold(
                onSuccess = { _signUpState.value = ResourceState.Success(it) },
                onFailure = { _signUpState.value = ResourceState.Error(it.message ?: "Sign up failed") }
            )
        }
    }
}