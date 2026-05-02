package com.example.finalprojectpam.data.model

data class Scene(
    val id: String,
    val order: Int,
    val narrative: String,
    val imageRes: String,
    val isEndScene: Boolean = false,
    val choices: List<Choice>
)
