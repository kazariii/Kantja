package com.example.finalprojectpam.ui.story

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.data.local.database.KancaDatabase
import com.example.finalprojectpam.data.local.entity.ScoreRecordEntity
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
    private val userRepository = UserRepository(KancaDatabase.getInstance(application))

    private val _currentStory = MutableStateFlow<Story?>(null)
    val currentStory: StateFlow<Story?> = _currentStory.asStateFlow()

    private val _currentScene = MutableStateFlow<Scene?>(null)
    val currentScene: StateFlow<Scene?> = _currentScene.asStateFlow()

    private val _totalScore = MutableStateFlow(0)
    val totalScore: StateFlow<Int> = _totalScore.asStateFlow()

    private val _isStoryFinished = MutableStateFlow(false)
    val isStoryFinished: StateFlow<Boolean> = _isStoryFinished.asStateFlow()

    fun loadStory(fileName: String) {
        val story = storyRepository.loadStory("stories/$fileName")
        _currentStory.value = story
        _currentScene.value = story?.scenes?.minByOrNull { it.order }
        _totalScore.value = 0
        _isStoryFinished.value = false
    }

    fun onChoiceSelected(choice: Choice) {
        _totalScore.value += choice.scoreValue

        val story = _currentStory.value ?: return
        if (choice.nextSceneId == null) {
            _isStoryFinished.value = true
            saveScore(story)
            return
        }

        val nextScene = story.scenes.find { it.id == choice.nextSceneId }
        if (nextScene == null || nextScene.isEndScene) {
            _isStoryFinished.value = true
            saveScore(story)
        } else {
            _currentScene.value = nextScene
        }
    }

    private fun saveScore(story: Story) {
        viewModelScope.launch {
            userRepository.saveScoreRecord(
                ScoreRecordEntity(
                    storyId = story.id,
                    storyTitle = story.title,
                    score = _totalScore.value,
                    maxScore = story.maxScore
                )
            )
        }
    }
}
