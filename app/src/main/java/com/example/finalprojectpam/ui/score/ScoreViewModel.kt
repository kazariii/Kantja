package com.example.finalprojectpam.ui.score

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.data.model.ScoreRecord
import com.example.finalprojectpam.data.model.Story
import com.example.finalprojectpam.data.model.UserProfile
import com.example.finalprojectpam.data.repository.StoryRepository
import com.example.finalprojectpam.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScoreViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()
    private val storyRepository = StoryRepository(application)

    private val _allScores = MutableStateFlow<List<ScoreRecord>>(emptyList())
    val allScores: StateFlow<List<ScoreRecord>> = _allScores.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _allStories = MutableStateFlow<List<Story>>(emptyList())
    val allStories: StateFlow<List<Story>> = _allStories.asStateFlow()

    init {
        _allStories.value = storyRepository.loadAllStories()
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _allScores.value = userRepository.getAllScores()
            _userProfile.value = userRepository.getUserProfile()
        }
    }
}
