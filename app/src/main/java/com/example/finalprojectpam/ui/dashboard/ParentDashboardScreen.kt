package com.example.finalprojectpam.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalprojectpam.data.local.entity.ScoreRecordEntity
import com.example.finalprojectpam.ui.auth.AuthViewModel
import com.example.finalprojectpam.ui.navigation.Screen
import com.example.finalprojectpam.ui.score.ScoreViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    scoreViewModel: ScoreViewModel = viewModel()
) {
    val scores by scoreViewModel.allScores.collectAsState()
    val userName = authViewModel.getUserName() ?: "Orang Tua"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard Orang Tua") },
                actions = {
                    TextButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.ParentDashboard.route) { inclusive = true }
                        }
                    }) {
                        Text("Keluar")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(text = "Halo, $userName!", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pantau perkembangan belajar anak di sini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                ProgressSummaryCard(scores = scores)
            }

            item {
                Text(text = "Riwayat Aktivitas", style = MaterialTheme.typography.titleMedium)
            }

            if (scores.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Belum ada aktivitas belajar anak.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(scores) { record ->
                    ActivityCard(record)
                }
            }
        }
    }
}

@Composable
private fun ProgressSummaryCard(scores: List<ScoreRecordEntity>) {
    val totalStories = scores.size
    val totalScore = scores.sumOf { it.score }
    val avgPct = if (totalStories > 0)
        scores.sumOf { (it.score.coerceAtLeast(0) * 100) / it.maxScore } / totalStories
    else 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem(value = "$totalStories", label = "Cerita Selesai")
            SummaryItem(value = "$totalScore", label = "Total Skor")
            SummaryItem(value = "$avgPct%", label = "Rata-rata")
        }
    }
}

@Composable
private fun SummaryItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ActivityCard(record: ScoreRecordEntity) {
    val pct = (record.score.coerceAtLeast(0) * 100) / record.maxScore
    val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id")).format(Date(record.completedAt))
    val performanceColor = when {
        pct >= 80 -> MaterialTheme.colorScheme.primary
        pct >= 50 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.error
    }
    val performanceLabel = when {
        pct >= 80 -> "Sangat Baik"
        pct >= 50 -> "Cukup Baik"
        else -> "Perlu Latihan"
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = record.storyTitle, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = performanceLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = performanceColor
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${record.score}/${record.maxScore}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "$pct%",
                    style = MaterialTheme.typography.labelMedium,
                    color = performanceColor
                )
            }
        }
    }
}
