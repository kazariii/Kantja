package com.example.finalprojectpam.data.repository

import com.example.finalprojectpam.data.model.ScoreRecord
import com.example.finalprojectpam.data.model.UserProfile
import com.example.finalprojectpam.data.remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class UserRepository {

    private val supabase = SupabaseClient.client

    suspend fun getUserProfile(): UserProfile? {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return null
        return supabase.postgrest["profiles"]
            .select { filter { eq("id", userId) } }
            .decodeSingleOrNull<UserProfile>()
    }

    suspend fun createOrUpdateProfile(name: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return
        val current = getUserProfile()
        supabase.postgrest["profiles"].upsert(
            UserProfile(
                id = userId,
                name = name,
                avatarRes = current?.avatarRes ?: "avatar_default",
                totalScore = current?.totalScore ?: 0,
                storiesCompleted = current?.storiesCompleted ?: 0
            )
        )
    }

    suspend fun saveScoreRecord(storyId: String, storyTitle: String, score: Int, maxScore: Int) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return
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
        val current = getUserProfile() ?: UserProfile(id = userId)
        supabase.postgrest["profiles"].upsert(
            current.copy(
                totalScore = current.totalScore + score,
                storiesCompleted = current.storiesCompleted + 1
            )
        )
    }

    suspend fun getAllScores(): List<ScoreRecord> {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return emptyList()
        return supabase.postgrest["score_records"]
            .select {
                filter { eq("user_id", userId) }
                order("completed_at", Order.DESCENDING)
            }
            .decodeList<ScoreRecord>()
    }
}
