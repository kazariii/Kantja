package com.example.finalprojectpam.ui.score

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalprojectpam.data.model.Story
import com.example.finalprojectpam.data.model.ScoreRecord
import com.example.finalprojectpam.ui.navigation.Screen

// ── Design tokens ──────────────────────────────────────────────────────────────
private val BgCream     = Color(0xFFFBF3E0)
private val Orange      = Color(0xFFF5A623)
private val OrangeDeep  = Color(0xFFED8A0F)
private val OrangeLight = Color(0xFFFFF4E0)
private val Brown       = Color(0xFF5C3A1E)
private val Brown2      = Color(0xFF7A4A2A)
private val TextSoft    = Color(0xFF8A6A4F)
private val Muted       = Color(0xFFB89A7C)

private fun categoryColor(category: String) = when (category.lowercase()) {
    "menabung"  -> Color(0xFFFFE7B8)
    "kejujuran" -> Color(0xFFFBDADF)
    "bijak"     -> Color(0xFFDCDFEE)
    "berbagi"   -> Color(0xFFEDDDF1)
    else        -> Color(0xFFFFD9A8)
}

private fun categoryEmoji(category: String) = when (category.lowercase()) {
    "menabung"  -> "🪙"
    "kejujuran" -> "⭐"
    "berbagi"   -> "❤️"
    "bijak"     -> "💡"
    else        -> "📖"
}

// ── Main screen ────────────────────────────────────────────────────────────────

@Composable
fun ScoreScreen(navController: NavController, viewModel: ScoreViewModel = viewModel()) {
    val allScores   by viewModel.allScores.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val allStories  by viewModel.allStories.collectAsState()

    val completedIds = remember(allScores) { allScores.map { it.storyId }.toSet() }

    // Badge list: semua story, tandai unlocked/locked
    data class Badge(val story: Story, val unlocked: Boolean, val record: ScoreRecord?)
    val badges = remember(allStories, completedIds, allScores) {
        allStories.map { story ->
            val record = allScores.firstOrNull { it.storyId == story.id }
            Badge(story, record != null, record)
        }
    }

    var activeFilter by remember { mutableStateOf("semua") }
    val filtered = remember(badges, activeFilter) {
        when (activeFilter) {
            "terbuka" -> badges.filter { it.unlocked }
            "terkunci" -> badges.filter { !it.unlocked }
            else -> badges
        }
    }

    // Level kalkulasi: tiap 100 XP = 1 level
    val totalXp   = userProfile?.totalScore ?: 0
    val level     = totalXp / 100 + 1
    val xpInLevel = totalXp % 100
    val xpToNext  = 100 - xpInLevel

    Scaffold(
        containerColor = BgCream,
        bottomBar = {
            PrestasiBottomBar(
                onBeranda  = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) { inclusive = true } } },
                onPrestasi = {},
                onLangkah  = { navController.navigate(Screen.Langkah.route) },
                onProfil   = { navController.navigate(Screen.Profile.route) }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Header ─────────────────────────────────────────────────────
            Column(Modifier.padding(horizontal = 20.dp)) {
                Text(
                    "Prestasi",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Brown
                )
                Text(
                    "Kumpulan badge yang sudah kamu raih",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSoft
                )
            }

            Spacer(Modifier.height(14.dp))

            // ── Level card ─────────────────────────────────────────────────
            Box(
                Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = OrangeDeep)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFFFFD9A8), Color(0xFFF5A623)))
                    )
                    .padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Kimo avatar
                    Box(
                        Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🐯", fontSize = 36.sp)
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            "Level $level",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            "$totalXp XP",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(Modifier.height(6.dp))
                        // XP progress bar
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White.copy(alpha = 0.45f))
                        ) {
                            Box(
                                Modifier
                                    .fillMaxWidth(xpInLevel / 100f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White)
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "$xpToNext XP lagi ke Level ${level + 1}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Filter chips ───────────────────────────────────────────────
            Row(
                Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "semua"   to "Semua · ${badges.size}",
                    "terbuka" to "Terbuka · ${badges.count { it.unlocked }}",
                    "terkunci" to "Terkunci · ${badges.count { !it.unlocked }}"
                ).forEach { (id, label) ->
                    val active = activeFilter == id
                    Box(
                        Modifier
                            .shadow(if (active) 3.dp else 1.dp, RoundedCornerShape(999.dp))
                            .clip(RoundedCornerShape(999.dp))
                            .background(if (active) Orange else Color.White)
                            .clickable { activeFilter = id }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (active) Color.White else Brown2
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Badge grid ─────────────────────────────────────────────────
            if (filtered.isEmpty()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (activeFilter == "terbuka") "Belum ada badge terbuka.\nYuk mainkan ceritanya dulu!"
                        else "Semua cerita sudah dimainkan!",
                        color = TextSoft,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            } else {
                // Grid 3 kolom — non-lazy karena sudah di dalam Column scroll
                val rows = filtered.chunked(3)
                Column(
                    Modifier.padding(horizontal = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rows.forEach { rowBadges ->
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowBadges.forEach { badge ->
                                BadgeCard(
                                    title = badge.story.title,
                                    category = badge.story.category,
                                    unlocked = badge.unlocked,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Padding kalau baris tidak penuh
                            repeat(3 - rowBadges.size) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BadgeCard(
    title: String,
    category: String,
    unlocked: Boolean,
    modifier: Modifier = Modifier
) {
    val bgColor  = if (unlocked) categoryColor(category) else Color(0xFFE5DBC8)
    val emoji    = if (unlocked) categoryEmoji(category) else "🔒"
    val alpha    = if (unlocked) 1f else 0.45f
    val gradient = if (unlocked)
        Brush.linearGradient(listOf(Color(0xFFFFE486), Orange))
    else
        Brush.linearGradient(listOf(Color(0xFFD0C4B0), Color(0xFFB8A898)))

    Box(
        modifier
            .alpha(alpha)
            .shadow(2.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Badge circle
            Box(
                Modifier
                    .size(64.dp)
                    .shadow(if (unlocked) 3.dp else 0.dp, CircleShape, spotColor = OrangeDeep)
                    .clip(CircleShape)
                    .background(gradient),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 28.sp)
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Brown,
                lineHeight = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

// ── Bottom nav (Prestasi active) ───────────────────────────────────────────────

@Composable
private fun PrestasiBottomBar(
    onBeranda: () -> Unit,
    onPrestasi: () -> Unit,
    onLangkah: () -> Unit,
    onProfil: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(96.dp)
            .navigationBarsPadding()
    ) {
        Surface(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(72.dp),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            shadowElevation = 8.dp
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomTab(Icons.Filled.Home,        "Beranda",  false, onBeranda)
                BottomTab(Icons.Filled.EmojiEvents, "Prestasi", true,  onPrestasi)
                Spacer(Modifier.width(64.dp))
                BottomTab(Icons.Filled.BarChart,    "Langkah",  false, onLangkah)
                BottomTab(Icons.Filled.Person,      "Profil",   false, onProfil)
            }
        }

        // Raised center button
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .size(76.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(64.dp)
                    .shadow(6.dp, CircleShape, spotColor = OrangeDeep)
                    .clip(CircleShape)
                    .background(Brush.verticalGradient(listOf(Color(0xFFFFB84D), Orange))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomTab(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    active: Boolean,
    onClick: () -> Unit
) {
    Column(
        Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (active) OrangeDeep else Muted,
            modifier = Modifier.size(22.dp)
        )
        Text(
            label,
            fontSize = 10.sp,
            fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Bold,
            color = if (active) OrangeDeep else Muted
        )
    }
}
