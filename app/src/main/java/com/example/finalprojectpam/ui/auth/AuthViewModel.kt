package com.example.finalprojectpam.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.finalprojectpam.data.local.preferences.AuthPreferences
import com.example.finalprojectpam.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AuthState {
    object Idle : AuthState()
    data class Success(val role: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(AuthPreferences(application))

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        _authState.value = when {
            email.isBlank() || password.isBlank() ->
                AuthState.Error("Email dan password tidak boleh kosong")
            !repository.isRegistered() ->
                AuthState.Error("Akun belum terdaftar. Silakan daftar terlebih dahulu.")
            !repository.login(email.trim(), password) ->
                AuthState.Error("Email atau password salah")
            else ->
                AuthState.Success(repository.getRole() ?: AuthPreferences.ROLE_CHILD)
        }
    }

    fun register(name: String, email: String, password: String, role: String) {
        _authState.value = when {
            name.isBlank() || email.isBlank() || password.isBlank() ->
                AuthState.Error("Semua kolom harus diisi")
            !email.contains("@") ->
                AuthState.Error("Format email tidak valid")
            password.length < 6 ->
                AuthState.Error("Password minimal 6 karakter")
            else -> {
                repository.register(name.trim(), email.trim(), password, role)
                AuthState.Success(role)
            }
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun checkSession(): String? =
        if (repository.isLoggedIn()) repository.getRole() else null

    fun getUserName(): String? = repository.getName()
}
