package com.example.finalprojectpam.ui.story

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.data.model.Choice
import com.example.finalprojectpam.data.model.Scene
import com.example.finalprojectpam.data.model.Story
import com.example.finalprojectpam.data.repository.StoryRepository
import com.example.finalprojectpam.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StoryViewModel(application: Application) : AndroidViewModel(application) {

    private val storyRepository = StoryRepository(application)
    private val userRepository = UserRepository()

    private val _currentStory = MutableStateFlow<Story?>(null)
    val currentStory: StateFlow<Story?> = _currentStory.asStateFlow()

    private val _currentScene = MutableStateFlow<Scene?>(null)
    val currentScene: StateFlow<Scene?> = _currentScene.asStateFlow()

    private val _totalScore = MutableStateFlow(0)
    val totalScore: StateFlow<Int> = _totalScore.asStateFlow()

    private val _isStoryFinished = MutableStateFlow(false)
    val isStoryFinished: StateFlow<Boolean> = _isStoryFinished.asStateFlow()

    private val _consequence = MutableStateFlow<ConsequenceState?>(null)
    val consequence: StateFlow<ConsequenceState?> = _consequence.asStateFlow()

    // Untuk progress bar
    private val _sceneIndex = MutableStateFlow(0)
    val sceneIndex: StateFlow<Int> = _sceneIndex.asStateFlow()

    private val _totalScenes = MutableStateFlow(0)
    val totalScenes: StateFlow<Int> = _totalScenes.asStateFlow()

    fun loadStory(fileName: String) {
        val story = storyRepository.loadStory("stories/$fileName.json")
        _currentStory.value = story
        _currentScene.value = story?.scenes?.minByOrNull { it.order }
        _totalScore.value = 0
        _isStoryFinished.value = false
        _consequence.value = null
        _sceneIndex.value = 0
        _totalScenes.value = story?.scenes?.size ?: 0
    }

    fun onChoiceSelected(choice: Choice) {
        _totalScore.value += choice.scoreValue

        val story = _currentStory.value ?: return
        val nextScene = if (choice.nextSceneId != null)
            story.scenes.find { it.id == choice.nextSceneId }
        else null

        val isLast = nextScene == null || nextScene.isEndScene

        _consequence.value = ConsequenceState(
            text = choice.consequence,
            scoreGained = choice.scoreValue,
            nextScene = nextScene,
            isLastScene = isLast
        )

        if (isLast) saveScore(story)
    }

    fun onConsequenceDismissed() {
        val state = _consequence.value ?: return
        _consequence.value = null

        if (state.isLastScene) {
            _isStoryFinished.value = true
        } else {
            _currentScene.value = state.nextScene
            _sceneIndex.value = _sceneIndex.value + 1
        }
    }

    private fun saveScore(story: Story) {
        viewModelScope.launch {
            userRepository.saveScoreRecord(
                storyId = story.id,
                storyTitle = story.title,
                score = _totalScore.value,
                maxScore = story.maxScore
            )
        }
    }
}

data class ConsequenceState(
    val text: String,
    val scoreGained: Int,
    val nextScene: Scene?,
    val isLastScene: Boolean
)
