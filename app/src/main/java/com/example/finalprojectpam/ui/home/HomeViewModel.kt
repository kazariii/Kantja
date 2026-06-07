package com.example.finalprojectpam.ui.home

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

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val storyRepository = StoryRepository(application)
    private val userRepository = UserRepository()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    private val _scoreRecords = MutableStateFlow<List<ScoreRecord>>(emptyList())
    val scoreRecords: StateFlow<List<ScoreRecord>> = _scoreRecords.asStateFlow()

    init {
        _stories.value = storyRepository.loadAllStories()
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _userProfile.value = userRepository.getUserProfile()
            _scoreRecords.value = userRepository.getAllScores()
        }
    }
}
