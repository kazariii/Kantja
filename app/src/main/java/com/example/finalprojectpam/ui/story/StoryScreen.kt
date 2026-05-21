package com.example.finalprojectpam.ui.story

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalprojectpam.data.model.Choice
import com.example.finalprojectpam.data.model.Story

// ── Design tokens ──────────────────────────────────────────────────────────────
private val Orange      = Color(0xFFF5A623)
private val OrangeDeep  = Color(0xFFED8A0F)
private val Brown       = Color(0xFF5C3A1E)
private val Brown2      = Color(0xFF7A4A2A)
private val Cream       = Color(0xFFFFF4E0)
private val BgCream     = Color(0xFFFBF3E0)
private val TextSoft    = Color(0xFF8A6A4F)

// ── Main screen ────────────────────────────────────────────────────────────────

@Composable
fun StoryScreen(
    navController: NavController,
    storyFileName: String,
    viewModel: StoryViewModel = viewModel()
) {
    val currentStory    by viewModel.currentStory.collectAsState()
    val currentScene    by viewModel.currentScene.collectAsState()
    val totalScore      by viewModel.totalScore.collectAsState()
    val isStoryFinished by viewModel.isStoryFinished.collectAsState()
    val consequence     by viewModel.consequence.collectAsState()
    val sceneIndex      by viewModel.sceneIndex.collectAsState()
    val totalScenes     by viewModel.totalScenes.collectAsState()

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

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFCB9E), Color(0xFFFFD9A8), Color(0xFFF5C99E))
                )
            )
    ) {
        // Ground
        GroundDecoration(Modifier.fillMaxSize())

        // Progress dots — top, di samping kanan tombol back
        StoryProgressBar(
            current = sceneIndex,
            total = totalScenes,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 18.dp, start = 64.dp, end = 20.dp)
        )

        // Back button — bulat putih
        Box(
            Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 14.dp, top = 10.dp)
                .size(44.dp)
                .shadow(4.dp, CircleShape)
                .background(Color.White, CircleShape)
                .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Kembali",
                tint = Brown2,
                modifier = Modifier.size(20.dp)
            )
        }

        // Narrative text — bold putih over gambar
        Text(
            text = currentScene?.narrative ?: "Memuat cerita...",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                shadow = Shadow(
                    color = Color(0xAA3A2410),
                    offset = Offset(0f, 2f),
                    blurRadius = 6f
                ),
                lineHeight = 28.sp
            ),
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(top = 76.dp, start = 20.dp, end = 20.dp)
        )

        // Bottom panel: pilihan atau consequence
        val cons = consequence
        if (cons != null) {
            ConsequencePanel(
                text = cons.text,
                scoreGained = cons.scoreGained,
                isLastScene = cons.isLastScene,
                onNext = { viewModel.onConsequenceDismissed() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 14.dp, vertical = 20.dp)
                    .navigationBarsPadding()
            )
        } else {
            ChoicePanel(
                choices = currentScene?.choices ?: emptyList(),
                onChoiceSelected = { viewModel.onChoiceSelected(it) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 14.dp, vertical = 20.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

// ── Progress bar ───────────────────────────────────────────────────────────────

@Composable
private fun StoryProgressBar(current: Int, total: Int, modifier: Modifier = Modifier) {
    if (total == 0) return
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { i ->
            val done = i <= current
            Box(
                Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (done) OrangeDeep else Color.White.copy(alpha = 0.7f))
            )
            if (i < total - 1) {
                Spacer(
                    Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (i < current) OrangeDeep else Color.White.copy(alpha = 0.7f))
                )
            }
        }
    }
}

// ── Choice panel ───────────────────────────────────────────────────────────────

@Composable
private fun ChoicePanel(
    choices: List<Choice>,
    onChoiceSelected: (Choice) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        // Coin badge + label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Box(
                Modifier
                    .size(44.dp)
                    .shadow(4.dp, CircleShape)
                    .background(
                        Brush.radialGradient(listOf(Color(0xFFFFE486), OrangeDeep)),
                        CircleShape
                    )
                    .border(2.dp, Brown2, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("Rp", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Brown2)
            }
            Text(
                "Apa pilihanmu?",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Brown,
                style = TextStyle(
                    shadow = Shadow(Color(0x55FFFFFF), Offset(0f, 1f), 0f)
                )
            )
        }

        // 2 kolom jika 2 pilihan, 1 kolom jika lebih/kurang
        if (choices.size == 2) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                choices.forEach { choice ->
                    ChoiceCard(
                        choice = choice,
                        modifier = Modifier.weight(1f),
                        onClick = { onChoiceSelected(choice) }
                    )
                }
            }
        } else {
            choices.forEach { choice ->
                ChoiceCard(
                    choice = choice,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onChoiceSelected(choice) }
                )
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun ChoiceCard(choice: Choice, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier
            .shadow(4.dp, RoundedCornerShape(18.dp), spotColor = Brown2)
            .clip(RoundedCornerShape(18.dp))
            .background(Cream)
            .border(3.dp, Brown2, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = choice.text,
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Brown,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

// ── Consequence panel ──────────────────────────────────────────────────────────

@Composable
private fun ConsequencePanel(
    text: String,
    scoreGained: Int,
    isLastScene: Boolean,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scoreColor = if (scoreGained >= 0) Color(0xFF4CAF50) else Color(0xFFE53935)
    val scoreLabel = when {
        scoreGained > 0 -> "+$scoreGained XP 🌟"
        scoreGained < 0 -> "$scoreGained XP"
        else -> null
    }

    Column(modifier) {
        Box(
            Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(22.dp))
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White)
                .padding(18.dp)
        ) {
            Column {
                Text(
                    "Akibat pilihanmu:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextSoft
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Brown,
                    lineHeight = 20.sp
                )
                if (scoreLabel != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        scoreLabel,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = scoreColor
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(999.dp), spotColor = OrangeDeep)
                .clip(RoundedCornerShape(999.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFFFFB84D), Orange)))
                .clickable(onClick = onNext)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isLastScene) "Lihat Hasil ›" else "Lanjut ›",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}

// ── Ground decoration ──────────────────────────────────────────────────────────

@Composable
private fun GroundDecoration(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        drawRect(
            color = Color(0xFFE8A86A),
            topLeft = Offset(0f, h * 0.72f),
            size = Size(w, h * 0.28f)
        )
        drawOval(
            color = Color(0xFFD89254),
            topLeft = Offset(-w * 0.1f, h * 0.68f),
            size = Size(w * 1.2f, h * 0.10f)
        )
    }
}

// ── Result screen (AchievementScreen) ─────────────────────────────────────────

@Composable
private fun StoryResultScreen(story: Story?, totalScore: Int, onBack: () -> Unit) {
    val maxScore  = story?.maxScore ?: 1
    val safeScore = totalScore.coerceAtLeast(0)
    val percentage = (safeScore * 100) / maxScore

    val resultLabel = when {
        percentage >= 80 -> "Luar biasa! Kamu sangat bijak!"
        percentage >= 50 -> "Bagus! Terus belajar ya!"
        else             -> "Yuk coba lagi, buat pilihan lebih bijak!"
    }
    val achievementLabel = when {
        percentage >= 80 -> "Pahlawan Bijak!"
        percentage >= 50 -> "Pejuang Baik!"
        else             -> "Tetap Semangat!"
    }
    val categoryLabel = when (story?.category?.lowercase()) {
        "menabung"  -> "Menabung"
        "kejujuran" -> "Kejujuran"
        "berbagi"   -> "Berbagi"
        "bijak"     -> "Bijak Belanja"
        else        -> story?.category?.replaceFirstChar { it.uppercase() } ?: "Keuangan"
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(BgCream)
    ) {
        ConfettiDecoration()

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(28.dp))

            // Kimo cheering
            ResultKimo(Modifier.size(150.dp))

            Spacer(Modifier.height(12.dp))

            Text(
                "Nilai Utama",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextSoft
            )
            Text(
                categoryLabel,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Orange
            )

            Spacer(Modifier.height(16.dp))

            // Score card
            Box(
                Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp))
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brown)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text("Skormu", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "$safeScore / $maxScore",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OrangeDeep
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        resultLabel,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Brown2,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Achievement badge card
            Box(
                Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(22.dp))
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFFFFF4E0))
                    .border(2.dp, Color(0xFFFFD9A8), RoundedCornerShape(22.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Prestasi Baru Diraih!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Brown2
                    )
                    Text(
                        achievementLabel,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OrangeDeep
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Total XP", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Brown2)
                    Text(
                        "+${safeScore} XP",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OrangeDeep
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Action buttons
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    Modifier
                        .weight(1f)
                        .shadow(4.dp, RoundedCornerShape(999.dp), spotColor = Orange)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.White)
                        .border(2.dp, Orange, RoundedCornerShape(999.dp))
                        .clickable { onBack() }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Beranda", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Orange)
                }
                Box(
                    Modifier
                        .weight(1f)
                        .shadow(4.dp, RoundedCornerShape(999.dp), spotColor = OrangeDeep)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Brush.verticalGradient(listOf(Color(0xFFFFB84D), Orange)))
                        .clickable { onBack() }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cerita Baru", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ── Decorations ────────────────────────────────────────────────────────────────

@Composable
private fun ConfettiDecoration() {
    val confettiColors = listOf(
        Color(0xFFFFB84D), Color(0xFFF8B8C2),
        Color(0xFF5478D4), Color(0xFF7BB661)
    )
    Canvas(Modifier.fillMaxSize()) {
        val positions = listOf(
            0.07f to 0.06f, 0.22f to 0.13f, 0.46f to 0.05f,
            0.68f to 0.10f, 0.88f to 0.07f, 0.14f to 0.21f,
            0.54f to 0.17f, 0.80f to 0.23f, 0.35f to 0.27f
        )
        positions.forEachIndexed { i, (xFrac, yFrac) ->
            val color = confettiColors[i % confettiColors.size]
            val sz = (8 + (i % 3) * 4).dp.toPx()
            val cx = xFrac * size.width
            val cy = yFrac * size.height
            rotate(i * 31f, Offset(cx, cy)) {
                drawRect(color.copy(alpha = 0.7f), Offset(cx, cy), Size(sz, sz * 0.4f))
            }
        }
    }
}

@Composable
private fun ResultKimo(modifier: Modifier = Modifier) {
    val inf = rememberInfiniteTransition(label = "bob")
    val dy by inf.animateFloat(
        0f, -8f,
        infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "dy"
    )
    Canvas(modifier.offset(y = dy.dp)) {
        val w = size.width; val cx = w / 2f; val cy = size.height * 0.52f; val r = w * 0.36f

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
        // Senyum lebar (pose senang)
        drawPath(
            Path().apply {
                moveTo(cx - r * .30f, cy + r * .26f)
                quadraticTo(cx, cy + r * .60f, cx + r * .30f, cy + r * .26f)
            },
            Color(0xFF3A2410),
            style = Stroke(w * .035f, cap = StrokeCap.Round)
        )
        // Tangan terangkat (pose sorak)
        drawLine(Color(0xFFF5A623), Offset(cx - r * .80f, cy + r * .2f), Offset(cx - r * 1.40f, cy - r * .6f), w * .12f, StrokeCap.Round)
        drawLine(Color(0xFFF5A623), Offset(cx + r * .80f, cy + r * .2f), Offset(cx + r * 1.40f, cy - r * .6f), w * .12f, StrokeCap.Round)
    }
}
