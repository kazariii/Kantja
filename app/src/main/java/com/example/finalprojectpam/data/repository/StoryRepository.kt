package com.example.finalprojectpam.data.repository

import android.content.Context
import com.example.finalprojectpam.data.model.Story
import com.google.gson.Gson

class StoryRepository(private val context: Context) {

    private val gson = Gson()

    fun loadStory(fileName: String): Story? {
        return try {
            val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
            gson.fromJson(json, Story::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun loadAllStories(): List<Story> {
        return try {
            context.assets.list("stories")
                ?.filter { it.endsWith(".json") }
                ?.mapNotNull { loadStory("stories/$it") }
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
