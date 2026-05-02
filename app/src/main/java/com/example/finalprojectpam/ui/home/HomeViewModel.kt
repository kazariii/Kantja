package com.example.finalprojectpam.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.data.local.database.KancaDatabase
import com.example.finalprojectpam.data.local.entity.UserProfileEntity
import com.example.finalprojectpam.data.model.Story
import com.example.finalprojectpam.data.repository.StoryRepository
import com.example.finalprojectpam.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val storyRepository = StoryRepository(application)
    private val userRepository = UserRepository(KancaDatabase.getInstance(application))

    val userProfile: StateFlow<UserProfileEntity?> = userRepository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    init {
        _stories.value = storyRepository.loadAllStories()
    }
}
