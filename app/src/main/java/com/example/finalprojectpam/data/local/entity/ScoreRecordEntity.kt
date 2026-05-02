package com.example.finalprojectpam.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_record")
data class ScoreRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val storyId: String,
    val storyTitle: String,
    val score: Int,
    val maxScore: Int,
    val completedAt: Long = System.currentTimeMillis()
)
