package com.example.finalprojectpam.utils

import android.content.Context
import com.example.finalprojectpam.data.model.Story
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonStoryLoader {

    private val gson = Gson()

    fun loadStory(context: Context, fileName: String): Story? {
        return try {
            val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
            gson.fromJson(json, Story::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun loadAllStories(context: Context): List<Story> {
        return try {
            context.assets.list("stories")
                ?.filter { it.endsWith(".json") }
                ?.mapNotNull { loadStory(context, "stories/$it") }
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun storyListFromJson(json: String): List<Story> {
        val type = object : TypeToken<List<Story>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
