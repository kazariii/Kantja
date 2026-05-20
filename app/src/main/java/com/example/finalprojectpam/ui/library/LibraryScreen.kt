package com.example.finalprojectpam.ui.library

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalprojectpam.data.model.Story
import com.example.finalprojectpam.ui.navigation.Screen

// ── Design tokens ──────────────────────────────────────────────────────────────
private val BgCream     = Color(0xFFFBF3E0)
private val Orange      = Color(0xFFF5A623)
private val OrangeDeep  = Color(0xFFED8A0F)
private val Brown       = Color(0xFF5C3A1E)
private val Brown2      = Color(0xFF7A4A2A)
private val TextSoft    = Color(0xFF8A6A4F)
private val Muted       = Color(0xFFB89A7C)

private val cardColors = listOf(
    Color(0xFFFFD9A8), Color(0xFFFFE7B8), Color(0xFFE5F0DC),
    Color(0xFFFCE3DA), Color(0xFFE1ECFB), Color(0xFFEDDDF1)
)

private fun categoryEmoji(category: String) = when (category.lowercase()) {
    "menabung"  -> "🪙"
    "kejujuran" -> "⭐"
    "berbagi"   -> "❤️"
    "bijak"     -> "💡"
    else        -> "📖"
}

private fun difficultyLabel(difficulty: String) = when (difficulty.lowercase()) {
    "mudah"  -> "Mudah"
    "sedang" -> "Sedang"
    "sulit"  -> "Sulit"
    else     -> difficulty.replaceFirstChar { it.uppercase() }
}

// ── Main screen ────────────────────────────────────────────────────────────────

@Composable
fun LibraryScreen(
    navController: NavController,
    viewModel: LibraryViewModel = viewModel()
) {
    val stories     by viewModel.stories.collectAsState()
    var activeFilter by remember { mutableStateOf("semua") }

    // Filter chips dibuat dinamis dari kategori yang ada
    val categories = remember(stories) {
        listOf("semua") + stories.map { it.category.lowercase() }.distinct().sorted()
    }

    val filtered = remember(stories, activeFilter) {
        if (activeFilter == "semua") stories
        else stories.filter { it.category.lowercase() == activeFilter }
    }

    Scaffold(
        containerColor = BgCream,
        bottomBar = {
            LibraryBottomBar(
                onBeranda  = { navController.popBackStack() },
                onPrestasi = { navController.navigate(Screen.Score.route) },
                onLangkah  = {},
                onProfil   = { navController.navigate(Screen.Profile.route) }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 14.dp, end = 14.dp, bottom = 24.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Header ────────────────────────────────────────────────────
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(Modifier.padding(top = 16.dp, start = 6.dp, end = 6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Tombol back bulat
                        Box(
                            Modifier
                                .size(40.dp)
                                .shadow(2.dp, CircleShape)
                                .background(Color(0xFFFFE7B8), CircleShape)
                                .clickable { navController.popBackStack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = OrangeDeep,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "Pustaka Cerita",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Brown
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "    Kurasi cerita pilihan untukmu",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSoft
                    )
                }
            }

            // ── Filter chips ──────────────────────────────────────────────
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    items(categories) { cat ->
                        val active = activeFilter == cat
                        val label = if (cat == "semua") "Semua"
                        else cat.replaceFirstChar { it.uppercase() }

                        Box(
                            Modifier
                                .shadow(
                                    if (active) 3.dp else 1.dp,
                                    RoundedCornerShape(999.dp),
                                    spotColor = if (active) OrangeDeep else Color.Transparent
                                )
                                .clip(RoundedCornerShape(999.dp))
                                .background(if (active) Orange else Color.White)
                                .clickable { activeFilter = cat }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
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
            }

            // ── Empty state ───────────────────────────────────────────────
            if (filtered.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Belum ada cerita untuk kategori ini.",
                            color = TextSoft,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // ── Story cards ───────────────────────────────────────────────
            itemsIndexed(filtered) { index, story ->
                LibraryStoryCard(
                    story = story,
                    colorIndex = index,
                    onClick = {
                        navController.navigate(
                            Screen.Story.createRoute("stories/${story.id}.json")
                        )
                    }
                )
            }
        }
    }
}

// ── Story card ─────────────────────────────────────────────────────────────────

@Composable
private fun LibraryStoryCard(story: Story, colorIndex: Int, onClick: () -> Unit) {
    val bgColor = cardColors[colorIndex % cardColors.size]
    val emoji   = categoryEmoji(story.category)

    Box(
        Modifier
            .shadow(4.dp, RoundedCornerShape(22.dp), spotColor = Brown2)
            .clip(RoundedCornerShape(22.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column {
            // Ilustrasi
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 46.sp)
            }

            Spacer(Modifier.height(8.dp))

            // Judul
            Text(
                text = story.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Brown,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(6.dp))

            // Kategori chip + difficulty
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "$emoji ${story.category.replaceFirstChar { it.uppercase() }}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Brown2
                    )
                }
                Text(
                    difficultyLabel(story.difficulty),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextSoft
                )
            }
        }
    }
}

// ── Bottom nav ─────────────────────────────────────────────────────────────────

@Composable
private fun LibraryBottomBar(
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
                BottomTab(Icons.Filled.Home,        "Beranda",  true,  onBeranda)
                BottomTab(Icons.Filled.EmojiEvents, "Prestasi", false, onPrestasi)
                Spacer(Modifier.width(64.dp))
                BottomTab(Icons.Filled.BarChart,    "Langkah",  false, onLangkah)
                BottomTab(Icons.Filled.Person,      "Profil",   false, onProfil)
            }
        }

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
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFFFFB84D), Orange))
                    )
                    .clickable { /* center action */ },
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
