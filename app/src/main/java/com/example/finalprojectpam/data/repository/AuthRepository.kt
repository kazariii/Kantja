package com.example.finalprojectpam.data.repository

import com.example.finalprojectpam.data.local.session.SimpleSession
import com.example.finalprojectpam.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

class AuthRepository {

    private val supabase = SupabaseClient.client

    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        val normalizedEmail = email.lowercase()
        val user = findUserByEmail(normalizedEmail)
            ?: error("Email belum terdaftar")

        if (user.password != password) {
            error("Password salah")
        }

        SimpleSession.saveUser(user.id)
    }

    suspend fun register(name: String, email: String, password: String): Result<Unit> = runCatching {
        val normalizedEmail = email.lowercase()
        val existingUser = findUserByEmail(normalizedEmail)
        if (existingUser != null) {
            error("Email sudah terdaftar")
        }

        val userId = UUID.randomUUID().toString()
        supabase.postgrest["app_users"].insert(
            AppUserInsert(
                id = userId,
                name = name,
                email = normalizedEmail,
                password = password
            )
        )
        SimpleSession.saveUser(userId)
    }

    suspend fun logout() {
        SimpleSession.clear()
    }

    fun isLoggedIn(): Boolean = SimpleSession.isLoggedIn()

    fun getCurrentUserId(): String? = SimpleSession.currentUserId()

    private suspend fun findUserByEmail(email: String): AppUserRecord? {
        return supabase.postgrest["app_users"]
            .select { filter { eq("email", email) } }
            .decodeSingleOrNull<AppUserRecord>()
    }
}

@Serializable
private data class AppUserRecord(
    val id: String,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    @SerialName("avatar_res") val avatarRes: String = "avatar_default",
    @SerialName("total_score") val totalScore: Int = 0,
    @SerialName("stories_completed") val storiesCompleted: Int = 0
)

@Serializable
private data class AppUserInsert(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    @SerialName("avatar_res") val avatarRes: String = "avatar_default",
    @SerialName("total_score") val totalScore: Int = 0,
    @SerialName("stories_completed") val storiesCompleted: Int = 0
)
