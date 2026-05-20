package com.example.finalprojectpam.ui.story

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalprojectpam.data.model.Story

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryScreen(
    navController: NavController,
    storyFileName: String,
    viewModel: StoryViewModel = viewModel()
) {
    val currentStory by viewModel.currentStory.collectAsState()
    val currentScene by viewModel.currentScene.collectAsState()
    val totalScore by viewModel.totalScore.collectAsState()
    val isStoryFinished by viewModel.isStoryFinished.collectAsState()
    val consequence by viewModel.consequence.collectAsState()

    LaunchedEffect(storyFileName) {
        viewModel.loadStory(storyFileName)
    }

    if (isStoryFinished) {
        StoryResultScreen(
            story = currentStory,
            totalScore = totalScore,
            onBack = { navController.popBackStack() }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentStory?.title ?: "Memuat...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Narrative card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = currentScene?.narrative ?: "Memuat cerita...",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (consequence != null) {
                // ── Tampilkan consequence ─────────────────────────────────────
                ConsequenceCard(
                    text = consequence!!.text,
                    scoreGained = consequence!!.scoreGained,
                    isLastScene = consequence!!.isLastScene,
                    onNext = { viewModel.onConsequenceDismissed() }
                )
            } else {
                // ── Tampilkan pilihan ─────────────────────────────────────────
                Text(
                    text = "Apa pilihanmu?",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                currentScene?.choices?.forEach { choice ->
                    Button(
                        onClick = { viewModel.onChoiceSelected(choice) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = choice.text, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
private fun ConsequenceCard(
    text: String,
    scoreGained: Int,
    isLastScene: Boolean,
    onNext: () -> Unit
) {
    val scoreLabel = when {
        scoreGained > 0 -> "+$scoreGained XP"
        scoreGained < 0 -> "$scoreGained XP"
        else -> null
    }
    val scoreColor = if (scoreGained >= 0)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.error

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Akibat pilihanmu:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            if (scoreLabel != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = scoreLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = scoreColor
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = if (isLastScene) "Lihat Hasil" else "Lanjut")
    }
}

@Composable
private fun StoryResultScreen(story: Story?, totalScore: Int, onBack: () -> Unit) {
    val maxScore = story?.maxScore ?: 1
    val safeScore = totalScore.coerceAtLeast(0)
    val percentage = (safeScore * 100) / maxScore

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Cerita Selesai!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = story?.title ?: "",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "$safeScore", style = MaterialTheme.typography.displayLarge)
        Text(
            text = "dari $maxScore poin",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        val label = when {
            percentage >= 80 -> "Luar biasa! Kamu sangat bijak!"
            percentage >= 50 -> "Bagus! Terus belajar ya!"
            else -> "Yuk coba lagi dan buat pilihan lebih bijak!"
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Kembali ke Beranda")
        }
    }
}
