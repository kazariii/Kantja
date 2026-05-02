package com.example.finalprojectpam.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.finalprojectpam.data.local.dao.ScoreRecordDao
import com.example.finalprojectpam.data.local.dao.UserProfileDao
import com.example.finalprojectpam.data.local.entity.ScoreRecordEntity
import com.example.finalprojectpam.data.local.entity.UserProfileEntity

@Database(
    entities = [UserProfileEntity::class, ScoreRecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class KancaDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun scoreRecordDao(): ScoreRecordDao

    companion object {
        @Volatile
        private var INSTANCE: KancaDatabase? = null

        fun getInstance(context: Context): KancaDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    KancaDatabase::class.java,
                    "kanca_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
