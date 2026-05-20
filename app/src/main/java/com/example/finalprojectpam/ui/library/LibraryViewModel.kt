package com.example.finalprojectpam.ui.library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.finalprojectpam.data.model.Story
import com.example.finalprojectpam.data.repository.StoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private val storyRepository = StoryRepository(application)

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    init {
        _stories.value = storyRepository.loadAllStories()
    }
}
