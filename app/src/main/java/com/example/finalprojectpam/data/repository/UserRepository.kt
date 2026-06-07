package com.example.finalprojectpam.data.repository

import com.example.finalprojectpam.data.model.ScoreRecord
import com.example.finalprojectpam.data.model.UserProfile
import com.example.finalprojectpam.data.local.session.SimpleSession
import com.example.finalprojectpam.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class UserRepository {

    private val supabase = SupabaseClient.client

    suspend fun getUserProfile(): UserProfile? {
        val userId = SimpleSession.currentUserId() ?: return null
        return supabase.postgrest["app_users"]
            .select { filter { eq("id", userId) } }
            .decodeSingleOrNull<UserProfile>()
    }

    suspend fun createOrUpdateProfile(name: String) {
        val userId = SimpleSession.currentUserId() ?: return
        supabase.postgrest["app_users"].update(
            UserProfileNameUpdate(name = name)
        ) {
            filter { eq("id", userId) }
        }
    }

    suspend fun saveScoreRecord(storyId: String, storyTitle: String, score: Int, maxScore: Int) {
        val userId = SimpleSession.currentUserId() ?: return
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        supabase.postgrest["score_records"].insert(
            ScoreRecord(
                id = UUID.randomUUID().toString(),
                userId = userId,
                storyId = storyId,
                storyTitle = storyTitle,
                score = score,
                maxScore = maxScore,
                completedAt = sdf.format(Date())
            )
        )
        val current = getUserProfile() ?: return
        supabase.postgrest["app_users"].update(
            UserProgressUpdate(
                totalScore = current.totalScore + score,
                storiesCompleted = current.storiesCompleted + 1
            )
        ) {
            filter { eq("id", userId) }
        }
    }

    suspend fun getAllScores(): List<ScoreRecord> {
        val userId = SimpleSession.currentUserId() ?: return emptyList()
        return supabase.postgrest["score_records"]
            .select {
                filter { eq("user_id", userId) }
                order("completed_at", Order.DESCENDING)
            }
            .decodeList<ScoreRecord>()
    }
}

@Serializable
private data class UserProfileNameUpdate(
    val name: String
)

@Serializable
private data class UserProgressUpdate(
    @SerialName("total_score") val totalScore: Int,
    @SerialName("stories_completed") val storiesCompleted: Int
)
