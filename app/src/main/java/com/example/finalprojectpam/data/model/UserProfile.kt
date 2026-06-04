package com.example.finalprojectpam.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    @SerialName("avatar_res") val avatarRes: String = "avatar_default",
    @SerialName("total_score") val totalScore: Int = 0,
    @SerialName("stories_completed") val storiesCompleted: Int = 0
)
