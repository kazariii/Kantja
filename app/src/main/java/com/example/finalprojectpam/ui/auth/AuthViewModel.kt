package com.example.finalprojectpam.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        when {
            email.isBlank() || password.isBlank() -> {
                _authState.value = AuthState.Error("Email dan password tidak boleh kosong")
                return
            }
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = repository.login(email.trim(), password)
            _authState.value = if (result.isSuccess) {
                AuthState.Success
            } else {
                AuthState.Error("Email atau password salah")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        when {
            name.isBlank() || email.isBlank() || password.isBlank() -> {
                _authState.value = AuthState.Error("Semua kolom harus diisi")
                return
            }
            !email.contains("@") -> {
                _authState.value = AuthState.Error("Format email tidak valid")
                return
            }
            password.length < 6 -> {
                _authState.value = AuthState.Error("Password minimal 6 karakter")
                return
            }
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = repository.register(name.trim(), email.trim(), password)
            _authState.value = if (result.isSuccess) {
                AuthState.Success
            } else {
                val msg = result.exceptionOrNull()?.message ?: "Gagal mendaftar"
                AuthState.Error(if (msg.contains("already registered", ignoreCase = true)) {
                    "Email sudah terdaftar"
                } else {
                    "Gagal mendaftar. Coba lagi."
                })
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthState.Idle
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun isLoggedIn(): Boolean = repository.isLoggedIn()
}
