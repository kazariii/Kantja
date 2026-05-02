package com.example.finalprojectpam.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val name: String,
    val avatarRes: String = "avatar_default",
    val totalScore: Int = 0,
    val storiesCompleted: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
