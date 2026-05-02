package com.example.finalprojectpam.data.model

data class Choice(
    val id: String,
    val text: String,
    val consequence: String,
    val scoreValue: Int,
    val nextSceneId: String?
)
