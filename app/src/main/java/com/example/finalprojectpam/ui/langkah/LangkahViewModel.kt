package com.example.finalprojectpam.ui.langkah

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.data.model.ScoreRecord
import com.example.finalprojectpam.data.model.UserProfile
import com.example.finalprojectpam.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LangkahViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _allScores = MutableStateFlow<List<ScoreRecord>>(emptyList())
    val allScores: StateFlow<List<ScoreRecord>> = _allScores.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _userProfile.value = userRepository.getUserProfile()
            _allScores.value   = userRepository.getAllScores()
        }
    }
}
