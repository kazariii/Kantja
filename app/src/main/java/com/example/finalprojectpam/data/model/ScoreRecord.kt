package com.example.finalprojectpam.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScoreRecord(
    val id: String = "",
    @SerialName("user_id") val userId: String = "",
    @SerialName("story_id") val storyId: String = "",
    @SerialName("story_title") val storyTitle: String = "",
    val score: Int = 0,
    @SerialName("max_score") val maxScore: Int = 0,
    @SerialName("completed_at") val completedAt: String = ""
)
