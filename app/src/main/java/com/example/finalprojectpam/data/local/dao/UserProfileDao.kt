package com.example.finalprojectpam.data.local.dao

import androidx.room.*
import com.example.finalprojectpam.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)

    @Update
    suspend fun update(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET totalScore = totalScore + :score WHERE id = 1")
    suspend fun addToTotalScore(score: Int)

    @Query("UPDATE user_profile SET storiesCompleted = storiesCompleted + 1 WHERE id = 1")
    suspend fun incrementStoriesCompleted()
}
