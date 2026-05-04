package com.example.finalprojectpam.data.repository

import com.example.finalprojectpam.data.model.UserProfile
import com.example.finalprojectpam.data.remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest

class AuthRepository {

    private val supabase = SupabaseClient.client

    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<Unit> = runCatching {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: error("Gagal mendapatkan ID pengguna setelah registrasi")
        supabase.postgrest["profiles"].upsert(
            UserProfile(id = userId, name = name)
        )
    }

    suspend fun logout() {
        supabase.auth.signOut()
    }

    fun isLoggedIn(): Boolean = supabase.auth.currentUserOrNull() != null

    fun getCurrentUserId(): String? = supabase.auth.currentUserOrNull()?.id
}
