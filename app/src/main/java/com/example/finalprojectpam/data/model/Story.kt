package com.example.finalprojectpam.data.model

data class Story(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val thumbnailRes: String,
    val maxScore: Int,
    val scenes: List<Scene>
)
