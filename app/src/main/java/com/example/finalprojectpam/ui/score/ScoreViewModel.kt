package com.example.finalprojectpam.ui.score

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.data.local.database.KancaDatabase
import com.example.finalprojectpam.data.local.entity.ScoreRecordEntity
import com.example.finalprojectpam.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ScoreViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(KancaDatabase.getInstance(application))

    val allScores: StateFlow<List<ScoreRecordEntity>> = userRepository.allScores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
