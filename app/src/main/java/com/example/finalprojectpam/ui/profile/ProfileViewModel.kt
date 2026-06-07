package com.example.finalprojectpam.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.data.model.UserProfile
import com.example.finalprojectpam.data.repository.AuthRepository
import com.example.finalprojectpam.data.repository.StoryRepository
import com.example.finalprojectpam.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()
    private val authRepository = AuthRepository()
    private val storyRepository = StoryRepository(application)

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _unlockedBadgeCount = MutableStateFlow(0)
    val unlockedBadgeCount: StateFlow<Int> = _unlockedBadgeCount.asStateFlow()

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut.asStateFlow()

    init {
        loadProfile()
    }

    // Dipanggil ulang setiap kali ProfileScreen ditampilkan,
    // agar storiesCompleted & totalScore selalu up-to-date.
    fun refreshProfile() {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _userProfile.value = userRepository.getUserProfile()
            val scores  = userRepository.getAllScores()
            val stories = storyRepository.loadAllStories()
            _unlockedBadgeCount.value = stories.count { story ->
                scores.any { it.storyId == story.id }
            }
        }
    }

    fun createOrUpdateProfile(name: String) {
        viewModelScope.launch {
            userRepository.createOrUpdateProfile(name)
            _userProfile.value = userRepository.getUserProfile()
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _isLoggedOut.value = true
        }
    }
}