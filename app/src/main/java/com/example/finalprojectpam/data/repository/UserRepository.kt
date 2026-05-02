package com.example.finalprojectpam.data.repository

import com.example.finalprojectpam.data.local.database.KancaDatabase
import com.example.finalprojectpam.data.local.entity.ScoreRecordEntity
import com.example.finalprojectpam.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val database: KancaDatabase) {

    val userProfile: Flow<UserProfileEntity?> = database.userProfileDao().getUserProfile()
    val allScores: Flow<List<ScoreRecordEntity>> = database.scoreRecordDao().getAllScores()

    suspend fun createOrUpdateProfile(name: String) {
        database.userProfileDao().insertOrUpdate(UserProfileEntity(name = name))
    }

    suspend fun saveScoreRecord(record: ScoreRecordEntity) {
        database.scoreRecordDao().insert(record)
        database.userProfileDao().addToTotalScore(record.score)
        database.userProfileDao().incrementStoriesCompleted()
    }

    suspend fun getLatestScoreForStory(storyId: String): ScoreRecordEntity? {
        return database.scoreRecordDao().getLatestScoreForStory(storyId)
    }
}
