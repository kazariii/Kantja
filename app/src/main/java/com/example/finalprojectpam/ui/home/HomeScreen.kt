package com.example.finalprojectpam.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalprojectpam.data.model.Story
import com.example.finalprojectpam.ui.auth.AuthViewModel
import com.example.finalprojectpam.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(),
    authViewModel: AuthViewModel
) {
    val stories by viewModel.stories.collectAsState()
    val userName = authViewModel.getUserName() ?: "Kanca"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KANCA") },
                actions = {
                    TextButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.ChildDashboard.route) { inclusive = true }
                        }
                    }) {
                        Text("Keluar")
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
            Text(
                text = "Halo, $userName!",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Yuk belajar keuangan hari ini!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                    Text("Profil")
                }
                OutlinedButton(onClick = { navController.navigate(Screen.Score.route) }) {
                    Text("Riwayat Nilai")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Pilih Cerita:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (stories.isEmpty()) {
                Text(
                    text = "Belum ada cerita tersedia.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(stories) { story ->
                        StoryCard(story = story) {
                            navController.navigate(Screen.Story.createRoute("${story.id}.json"))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StoryCard(story: Story, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = story.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = story.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SuggestionChip(onClick = {}, label = { Text(story.category) })
                SuggestionChip(onClick = {}, label = { Text(story.difficulty) })
                SuggestionChip(onClick = {}, label = { Text("Maks: ${story.maxScore} poin") })
            }
        }
    }
}
