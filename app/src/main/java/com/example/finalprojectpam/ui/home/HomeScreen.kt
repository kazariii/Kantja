package com.example.finalprojectpam.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalprojectpam.data.model.ScoreRecord
import com.example.finalprojectpam.data.model.Story
import com.example.finalprojectpam.ui.auth.AuthViewModel
import com.example.finalprojectpam.ui.navigation.Screen
import java.util.Calendar

// ── Design tokens ──────────────────────────────────────────────────────────────
private val BgCream     = Color(0xFFFBF3E0)
private val Orange      = Color(0xFFF5A623)
private val OrangeDeep  = Color(0xFFED8A0F)
private val OrangeLight = Color(0xFFFFF4E0)
private val Brown       = Color(0xFF5C3A1E)
private val Brown2      = Color(0xFF7A4A2A)
private val TextSoft    = Color(0xFF8A6A4F)
private val Muted       = Color(0xFFB89A7C)

private val storyCardColors = listOf(
    Color(0xFFFFD9A8), Color(0xFFFFE7B8), Color(0xFFE1ECFB),
    Color(0xFFE5F0DC), Color(0xFFFCE3DA)
)

// ── Main screen ────────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(),
    authViewModel: AuthViewModel
) {
    val stories by viewModel.stories.collectAsState()
    val scoreRecords by viewModel.scoreRecords.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val storyToStart = remember(stories, scoreRecords) {
        findStoryToStart(stories, scoreRecords)
    }

    val firstName = userProfile?.name?.split(" ")?.firstOrNull() ?: "Kanca"
    val totalScore = userProfile?.totalScore ?: 0
    val storiesCompleted = userProfile?.storiesCompleted ?: 0

    val greeting = remember {
        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..10  -> "Selamat Pagi"
            in 11..14 -> "Selamat Siang"
            in 15..17 -> "Selamat Sore"
            else      -> "Selamat Malam"
        }
    }

    Scaffold(
        containerColor = BgCream,
        bottomBar = {
            KancaBottomBar(
                active = "beranda",
                onBeranda = {},
                onPrestasi = { navController.navigate(Screen.Score.route) },
                onBuat     = { navController.navigate(Screen.Library.route) },
                onLangkah  = { navController.navigate(Screen.Langkah.route) },
                onProfil   = { navController.navigate(Screen.Profile.route) }
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background decorations
            Sparkle(
                Modifier.align(Alignment.TopEnd).offset(x = (-30).dp, y = 80.dp),
                size = 20.dp, color = Color(0xFFFFB84D)
            )
            CloudShape(Modifier.offset(x = 30.dp, y = 70.dp))

            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 56.dp, bottom = 24.dp)
            ) {
                // ── Header ─────────────────────────────────────────────────────
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            greeting,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSoft
                        )
                        Text(
                            "Hai, $firstName! 👋",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Brown
                        )
                    }
                    // Avatar → Profile
                    Box(
                        Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFE7B8))
                            .clickable { navController.navigate(Screen.Profile.route) },
                        contentAlignment = Alignment.Center
                    ) {
                        KimoHead(Modifier.size(38.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Greeting banner ─────────────────────────────────────────────
                Box(
                    Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = OrangeDeep)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFFFD9A8), Color(0xFFFFC078)),
                                start = Offset(0f, 0f),
                                end = Offset(Float.MAX_VALUE, Float.MAX_VALUE)
                            )
                        )
                        .heightIn(min = 130.dp)
                ) {
                    // Sparkles inside banner
                    Sparkle(Modifier.offset(14.dp, 14.dp), 20.dp, Color(0xFFFFE486))
                    Sparkle(Modifier.offset(70.dp, 62.dp), 14.dp, Color(0xFFFFE486))

                    Row(Modifier.fillMaxWidth()) {
                        Column(
                            Modifier
                                .weight(1f)
                                .padding(start = 18.dp, top = 18.dp, bottom = 18.dp, end = 4.dp)
                        ) {
                            Text(
                                "Yuk lanjut petualangan!",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Brown
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Kimo sudah menunggumu untuk membaca cerita baru.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Brown2,
                                lineHeight = 16.sp
                            )
                            Spacer(Modifier.height(12.dp))
                            Box(
                                Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(Orange)
                                    .clickable {
                                        storyToStart?.let {
                                            navController.navigate(Screen.Story.createRoute(it.id))
                                        }
                                    }
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    "Mulai Cerita",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }
                        // Kimo mascot, peek from right-bottom
                        Box(
                            Modifier
                                .width(110.dp)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            KimoBobbing(Modifier.size(130.dp).offset(x = 10.dp, y = 10.dp))
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // ── Stats row ───────────────────────────────────────────────────
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        Modifier.weight(1f),
                        icon = { Text("🔥", fontSize = 22.sp) },
                        value = "$storiesCompleted",
                        label = "Selesai"
                    )
                    StatCard(
                        Modifier.weight(1f),
                        bgColor = OrangeLight,
                        icon = { Sparkle(Modifier.size(22.dp), 22.dp, Orange) },
                        value = "$totalScore",
                        label = "Total XP",
                        valueColor = OrangeDeep
                    )
                    StatCard(
                        Modifier.weight(1f),
                        icon = { Text("🌟", fontSize = 20.sp) },
                        value = if (storiesCompleted > 0) "Aktif" else "Baru",
                        label = "Status",
                        valueFontSize = 14.sp
                    )
                }

                Spacer(Modifier.height(20.dp))

                // ── Story recommendations ───────────────────────────────────────
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Rekomendasi Cerita",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Brown
                    )
                    Text(
                        "Lihat semua ›",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OrangeDeep,
                        modifier = Modifier.clickable { navController.navigate(Screen.Library.route) }
                    )
                }

                Spacer(Modifier.height(10.dp))

                if (stories.isEmpty()) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Belum ada cerita tersedia.",
                            color = TextSoft,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(stories) { index, story ->
                            StoryCard(story, index) {
                                navController.navigate(Screen.Story.createRoute(story.id))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Keluar (subtle, di bagian bawah)
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TextButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }) {
                        Text("Keluar", color = Muted, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// ── Bottom nav bar ─────────────────────────────────────────────────────────────

private fun findStoryToStart(stories: List<Story>, scoreRecords: List<ScoreRecord>): Story? {
    if (stories.isEmpty()) return null

    val bestScoreByStory = scoreRecords
        .groupBy { it.storyId }
        .mapValues { (_, records) -> records.maxOf { it.score } }

    return stories.firstOrNull { story ->
        val bestScore = bestScoreByStory[story.id]
        bestScore == null || bestScore < story.maxScore
    } ?: scoreRecords.firstOrNull()?.let { latestScore ->
        stories.firstOrNull { it.id == latestScore.storyId }
    } ?: stories.last()
}

@Composable
private fun KancaBottomBar(
    active: String,
    onBeranda: () -> Unit,
    onPrestasi: () -> Unit,
    onBuat: () -> Unit,
    onLangkah: () -> Unit,
    onProfil: () -> Unit
) {
    // Extra height so the raised center button doesn't get clipped
    Box(
        Modifier
            .fillMaxWidth()
            .height(96.dp)
            .navigationBarsPadding()
    ) {
        // White bar (bottom 72 dp)
        Surface(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(72.dp),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            shadowElevation = 8.dp,
            tonalElevation = 0.dp
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomTab(Icons.Filled.Home,         "Beranda",  active == "beranda",  onBeranda)
                BottomTab(Icons.Filled.EmojiEvents,  "Prestasi", active == "prestasi", onPrestasi)
                Spacer(Modifier.width(64.dp))   // room for the raised button
                BottomTab(Icons.Filled.BarChart,      "Langkah",  active == "langkah",  onLangkah)
                BottomTab(Icons.Filled.Person,       "Profil",   active == "profil",   onProfil)
            }
        }

        // Raised center button — white ring then orange circle
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
                    .clickable(onClick = onBuat),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = "Buat Cerita",
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

// ── Story card ─────────────────────────────────────────────────────────────────

@Composable
private fun StoryCard(story: Story, index: Int, onClick: () -> Unit) {
    val bg = storyCardColors[index % storyCardColors.size]
    val context = LocalContext.current
    val thumbnailRes = remember(story.thumbnailRes) {
        context.resources.getIdentifier(story.thumbnailRes, "drawable", context.packageName)
            .takeIf { it != 0 }
    }

    Box(
        Modifier
            .width(170.dp)
            .clip(RoundedCornerShape(22.dp))
            .shadow(4.dp, RoundedCornerShape(22.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column {
            // Thumbnail
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                if (thumbnailRes != null) {
                    Image(
                        painter = painterResource(thumbnailRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("📖", fontSize = 42.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                story.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Brown,
                maxLines = 1
            )

            Spacer(Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        story.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Brown2
                    )
                }
                Text(story.difficulty, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSoft)
            }
        }
    }
}

// ── Stat card ──────────────────────────────────────────────────────────────────

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    bgColor: Color = Color.White,
    icon: @Composable () -> Unit,
    value: String,
    label: String,
    valueColor: Color = Brown,
    valueFontSize: TextUnit = 18.sp
) {
    Surface(
        modifier,
        shape = RoundedCornerShape(22.dp),
        color = bgColor,
        shadowElevation = 2.dp
    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = valueFontSize, fontWeight = FontWeight.ExtraBold, color = valueColor)
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSoft)
        }
    }
}

// ── Canvas decorations ─────────────────────────────────────────────────────────

@Composable
private fun Sparkle(modifier: Modifier, size: Dp, color: Color) {
    Canvas(modifier.size(size)) {
        val s = this.size.width
        val path = Path().apply {
            moveTo(s / 2f, 0f)
            lineTo(s * .6f, s * .4f); lineTo(s, s / 2f)
            lineTo(s * .6f, s * .6f); lineTo(s / 2f, s)
            lineTo(s * .4f, s * .6f); lineTo(0f, s / 2f)
            lineTo(s * .4f, s * .4f); close()
        }
        drawPath(path, color)
    }
}

@Composable
private fun CloudShape(modifier: Modifier = Modifier) {
    Canvas(modifier.size(70.dp).alpha(0.65f)) {
        val w = size.width; val h = size.height
        val c = Color.White
        drawCircle(c, w * .20f, Offset(w * .22f, h * .55f))
        drawCircle(c, w * .26f, Offset(w * .44f, h * .42f))
        drawCircle(c, w * .22f, Offset(w * .66f, h * .48f))
        drawCircle(c, w * .17f, Offset(w * .83f, h * .58f))
        drawRoundRect(c, Offset(w * .05f, h * .54f), Size(w * .88f, h * .46f), CornerRadius(w * .1f))
    }
}

// ── Kimo drawings ──────────────────────────────────────────────────────────────

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawKimoFace() {
    val w = size.width
    val cx = w / 2f
    val cy = size.height * 0.52f
    val r = w * 0.36f

    fun ear(left: Boolean): Path {
        val sx = if (left) -1f else 1f
        return Path().apply {
            moveTo(cx + sx * r * .75f, cy - r * .60f)
            lineTo(cx + sx * r * .45f, cy - r * 1.28f)
            lineTo(cx + sx * r * .10f, cy - r * .70f)
            close()
        }
    }
    drawPath(ear(true),  Color(0xFFF5A623))
    drawPath(ear(true),  Color(0xFF7A4A2A), style = Stroke(w * .03f))
    drawPath(ear(false), Color(0xFFF5A623))
    drawPath(ear(false), Color(0xFF7A4A2A), style = Stroke(w * .03f))

    drawCircle(
        Brush.radialGradient(
            listOf(Color(0xFFFFC078), Color(0xFFF5A623), Color(0xFFD9881A)),
            Offset(cx, cy - r * .2f), r * 1.3f
        ),
        r, Offset(cx, cy)
    )
    drawCircle(Color(0xFF7A4A2A), r, Offset(cx, cy), style = Stroke(w * .03f))
    drawOval(Color(0xFFFFF1D8), Offset(cx - r * .58f, cy - r * .18f), Size(r * 1.16f, r * .86f))
    drawOval(Color(0xFFFFB1A0).copy(alpha = .7f), Offset(cx - r * .92f, cy - r * .08f), Size(r * .36f, r * .24f))
    drawOval(Color(0xFFFFB1A0).copy(alpha = .7f), Offset(cx + r * .56f, cy - r * .08f), Size(r * .36f, r * .24f))
    drawOval(Color(0xFF3A2410), Offset(cx - r * .46f, cy - r * .42f), Size(r * .28f, r * .34f))
    drawCircle(Color.White, r * .07f, Offset(cx - r * .38f, cy - r * .50f))
    drawOval(Color(0xFF3A2410), Offset(cx + r * .18f, cy - r * .42f), Size(r * .28f, r * .34f))
    drawCircle(Color.White, r * .07f, Offset(cx + r * .26f, cy - r * .50f))
    drawOval(Color(0xFF3A2410), Offset(cx - r * .10f, cy + r * .04f), Size(r * .20f, r * .14f))
    drawPath(
        Path().apply {
            moveTo(cx - r * .26f, cy + r * .28f)
            quadraticTo(cx, cy + r * .52f, cx + r * .26f, cy + r * .28f)
        },
        Color(0xFF3A2410),
        style = Stroke(w * .03f, cap = StrokeCap.Round)
    )
}

/** Static Kimo head for avatar. */
@Composable
private fun KimoHead(modifier: Modifier = Modifier) {
    Canvas(modifier) { drawKimoFace() }
}

/** Bobbing Kimo for the greeting banner. */
@Composable
private fun KimoBobbing(modifier: Modifier = Modifier) {
    val inf = rememberInfiniteTransition(label = "bob")
    val dy by inf.animateFloat(
        0f, -8f,
        infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "dy"
    )
    Canvas(modifier.offset(y = dy.dp)) { drawKimoFace() }
}
