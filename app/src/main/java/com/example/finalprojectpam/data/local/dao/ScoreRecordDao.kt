package com.example.finalprojectpam.data.local.dao

import androidx.room.*
import com.example.finalprojectpam.data.local.entity.ScoreRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreRecordDao {

    @Query("SELECT * FROM score_record ORDER BY completedAt DESC")
    fun getAllScores(): Flow<List<ScoreRecordEntity>>

    @Query("SELECT * FROM score_record WHERE storyId = :storyId ORDER BY completedAt DESC LIMIT 1")
    suspend fun getLatestScoreForStory(storyId: String): ScoreRecordEntity?

    @Insert
    suspend fun insert(record: ScoreRecordEntity)

    @Query("DELETE FROM score_record")
    suspend fun deleteAll()
}
