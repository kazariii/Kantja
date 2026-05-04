package com.example.finalprojectpam.ui.score

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.data.model.ScoreRecord
import com.example.finalprojectpam.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScoreViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()

    private val _allScores = MutableStateFlow<List<ScoreRecord>>(emptyList())
    val allScores: StateFlow<List<ScoreRecord>> = _allScores.asStateFlow()

    init {
        loadScores()
    }

    private fun loadScores() {
        viewModelScope.launch {
            _allScores.value = userRepository.getAllScores()
        }
    }
}
