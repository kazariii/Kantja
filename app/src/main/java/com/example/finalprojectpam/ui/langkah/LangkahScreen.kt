package com.example.finalprojectpam.ui.langkah

import android.graphics.Paint as NativePaint
import android.graphics.Typeface
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
private val GridLine    = Color(0xFFF0E1C4)

// ── Main screen ────────────────────────────────────────────────────────────────

@Composable
fun LangkahScreen(
    navController: NavController,
    viewModel: LangkahViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val allScores   by viewModel.allScores.collectAsState()

    val storiesCompleted = userProfile?.storiesCompleted ?: 0
    val totalXp          = userProfile?.totalScore ?: 0

    // Rata-rata persentase skor dari semua riwayat
    val avgPercent = remember(allScores) {
        if (allScores.isEmpty()) 0
        else allScores.map { (it.score.coerceAtLeast(0) * 100) / it.maxScore.coerceAtLeast(1) }
            .average().toInt()
    }

    // Data chart: max 7 cerita terakhir (urutan terlama → terbaru)
    val chartPoints = remember(allScores) {
        allScores.takeLast(7).map { record ->
            val pct = (record.score.coerceAtLeast(0) * 100) / record.maxScore.coerceAtLeast(1)
            val shortLabel = record.storyTitle.split(" ").firstOrNull()?.take(5) ?: "?"
            ChartPoint(shortLabel, pct.toFloat())
        }
    }

    Scaffold(
        containerColor = BgCream,
        bottomBar = {
            LangkahBottomBar(
                onBeranda  = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) { inclusive = true } } },
                onPrestasi = { navController.navigate(Screen.Score.route) },
                onLangkah  = {},
                onProfil   = { navController.navigate(Screen.Profile.route) }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // ── Banner mascot ───────────────────────────────────────────────
            MascotBanner()

            Spacer(Modifier.height(14.dp))

            // ── 3 stat tiles ────────────────────────────────────────────────
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatTile(
                    modifier = Modifier.weight(1f),
                    icon = { XpIcon() },
                    value = "$totalXp",
                    label = "Total XP"
                )
                StatTile(
                    modifier = Modifier.weight(1f),
                    icon = { BookIcon() },
                    value = "$storiesCompleted",
                    label = "Cerita Selesai"
                )
                StatTile(
                    modifier = Modifier.weight(1f),
                    icon = { StarIcon() },
                    value = "$avgPercent%",
                    label = "Tingkat Sukses"
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Progres Skor chart ──────────────────────────────────────────
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "Progres Skor",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Brown
                )
            }

            Spacer(Modifier.height(10.dp))

            Surface(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "${allScores.size} cerita dimainkan",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSoft
                            )
                            Text(
                                "$avgPercent% rata-rata",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Brown
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    if (chartPoints.isEmpty()) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Mainkan cerita dulu\nuntuk melihat progres!",
                                color = TextSoft,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        ScoreLineChart(
                            points = chartPoints,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // ── Tingkat Kesuksesan ──────────────────────────────────────────
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "Tingkat Kesuksesan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Brown
                )
                // Mini Kimo
                Text("🐯", fontSize = 32.sp)
            }

            Spacer(Modifier.height(10.dp))

            Surface(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Keseluruhan",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Brown2
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            Modifier
                                .weight(1f)
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(OrangeLight)
                        ) {
                            Box(
                                Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(avgPercent / 100f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFFFFB84D), Orange)
                                        )
                                    )
                            )
                        }
                        Text(
                            "$avgPercent%",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OrangeDeep
                        )
                    }

                    // Per kategori jika ada data
                    if (allScores.isNotEmpty()) {
                        Spacer(Modifier.height(14.dp))
                        val categoryScores = allScores.groupBy { it.storyTitle }
                        val best = allScores.maxByOrNull {
                            (it.score.coerceAtLeast(0) * 100) / it.maxScore.coerceAtLeast(1)
                        }
                        if (best != null) {
                            val bestPct = (best.score.coerceAtLeast(0) * 100) / best.maxScore.coerceAtLeast(1)
                            Text(
                                "🏆 Skor terbaik: ${best.storyTitle}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Brown2
                            )
                            Spacer(Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    Modifier
                                        .weight(1f)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(OrangeLight)
                                ) {
                                    Box(
                                        Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(bestPct / 100f)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFF7BB661))
                                    )
                                }
                                Text(
                                    "$bestPct%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF4D7A3A)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ── Banner mascot ──────────────────────────────────────────────────────────────

@Composable
private fun MascotBanner() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(130.dp)
            .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = OrangeDeep)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFFFC078), Orange),
                    start = Offset(0f, 0f),
                    end = Offset(Float.MAX_VALUE, Float.MAX_VALUE)
                )
            )
    ) {
        // Sparkles
        SparkleAt(0.14f, 0.28f, 26f, Color(0xFFFFE486))
        SparkleAt(0.30f, 0.80f, 18f, Color(0xFFFFE486))
        SparkleAt(0.80f, 0.18f, 16f, Color(0xFFFFE486))
        SparkleAt(0.86f, 0.70f, 22f, Color(0xFFFFE486))

        // Label teks
        Column(
            Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp)
        ) {
            Text(
                "Statistikmu",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                "Pantau perkembanganmu\nbersama Kimo!",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.85f),
                lineHeight = 16.sp
            )
        }

        // Kimo emoji di kanan
        Text(
            "🐯",
            fontSize = 72.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 8.dp, y = 10.dp)
        )
    }
}

@Composable
private fun SparkleAt(xFrac: Float, yFrac: Float, size: Float, color: Color) {
    Canvas(Modifier.fillMaxSize()) {
        val cx = this.size.width * xFrac
        val cy = this.size.height * yFrac
        val s  = size
        val path = Path().apply {
            moveTo(cx, cy - s / 2)
            lineTo(cx + s * .1f, cy - s * .1f); lineTo(cx + s / 2, cy)
            lineTo(cx + s * .1f, cy + s * .1f); lineTo(cx, cy + s / 2)
            lineTo(cx - s * .1f, cy + s * .1f); lineTo(cx - s / 2, cy)
            lineTo(cx - s * .1f, cy - s * .1f); close()
        }
        drawPath(path, color)
    }
}

// ── Stat tiles ─────────────────────────────────────────────────────────────────

@Composable
private fun StatTile(
    modifier: Modifier,
    icon: @Composable () -> Unit,
    value: String,
    label: String
) {
    Surface(
        modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.5.dp, Color(0xFFF5DBB0)),
        shadowElevation = 1.dp
    ) {
        Column(
            Modifier.padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            Spacer(Modifier.height(6.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Brown, lineHeight = 20.sp)
            Spacer(Modifier.height(2.dp))
            Text(
                label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Brown2,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center, lineHeight = 12.sp
            )
        }
    }
}

@Composable
private fun XpIcon() {
    Canvas(Modifier.size(34.dp)) {
        val cx = size.width / 2f; val cy = size.height / 2f; val r = size.width * 0.42f
        drawCircle(Color(0xFFFFD24A), r, Offset(cx, cy))
        drawCircle(Color(0xFF7A4A2A), r, Offset(cx, cy), style = Stroke(size.width * 0.065f))
        val star = Path().apply {
            val pts = 5; val outerR = r * 0.65f; val innerR = r * 0.32f
            for (i in 0 until pts * 2) {
                val angle = Math.PI * i / pts - Math.PI / 2
                val rad   = if (i % 2 == 0) outerR else innerR
                val x     = cx + (rad * Math.cos(angle)).toFloat()
                val y     = cy + (rad * Math.sin(angle)).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }
        drawPath(star, Color(0xFFED8A0F))
    }
}

@Composable
private fun BookIcon() {
    Canvas(Modifier.size(34.dp)) {
        val w = size.width; val h = size.height
        val path = Path().apply {
            moveTo(w * .15f, h * .2f)
            quadraticTo(w * .35f, h * .15f, w / 2, h * .25f)
            quadraticTo(w * .65f, h * .15f, w * .85f, h * .2f)
            lineTo(w * .85f, h * .8f)
            quadraticTo(w * .65f, h * .75f, w / 2, h * .85f)
            quadraticTo(w * .35f, h * .75f, w * .15f, h * .8f)
            close()
        }
        drawPath(path, Color(0xFFFFE0B0))
        drawPath(path, Color(0xFF7A4A2A), style = Stroke(w * .065f, join = StrokeJoin.Round))
        drawLine(Color(0xFF7A4A2A), Offset(w / 2, h * .25f), Offset(w / 2, h * .85f), w * .065f)
    }
}

@Composable
private fun StarIcon() {
    Canvas(Modifier.size(34.dp)) {
        val cx = size.width / 2f; val cy = size.height / 2f
        val path = Path().apply {
            val pts = 5; val outerR = size.width * 0.44f; val innerR = size.width * 0.20f
            for (i in 0 until pts * 2) {
                val angle = Math.PI * i / pts - Math.PI / 2
                val rad   = if (i % 2 == 0) outerR else innerR
                val x     = cx + (rad * Math.cos(angle)).toFloat()
                val y     = cy + (rad * Math.sin(angle)).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }
        drawPath(path, Color(0xFFFFD24A))
        drawPath(path, Color(0xFF7A4A2A), style = Stroke(size.width * 0.065f, join = StrokeJoin.Round))
    }
}

// ── Line chart ─────────────────────────────────────────────────────────────────

data class ChartPoint(val label: String, val value: Float)

@Composable
private fun ScoreLineChart(points: List<ChartPoint>, modifier: Modifier = Modifier) {
    val orangeColor  = Orange
    val gridColor    = GridLine
    val dotFill      = Orange
    val tooltipColor = Brown

    Canvas(modifier) {
        val padL = 36.dp.toPx()
        val padR = 8.dp.toPx()
        val padT = 8.dp.toPx()
        val padB = 22.dp.toPx()
        val innerW = size.width - padL - padR
        val innerH = size.height - padT - padB

        // Y grid lines + labels (0, 25, 50, 75, 100)
        val ySteps = listOf(0f, 25f, 50f, 75f, 100f)
        val nativePaint = NativePaint().apply {
            textSize  = 9.dp.toPx()
            typeface  = Typeface.DEFAULT_BOLD
            color     = android.graphics.Color.argb(180, 138, 106, 79)
            textAlign = NativePaint.Align.RIGHT
            isAntiAlias = true
        }
        ySteps.forEach { v ->
            val yPos = padT + (1f - v / 100f) * innerH
            // grid line
            drawLine(
                color = gridColor,
                start = Offset(padL, yPos),
                end   = Offset(size.width - padR, yPos),
                strokeWidth = 1.dp.toPx(),
                pathEffect  = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                    floatArrayOf(4.dp.toPx(), 3.dp.toPx())
                )
            )
            // y label
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    "${v.toInt()}",
                    padL - 4.dp.toPx(),
                    yPos + 3.dp.toPx(),
                    nativePaint
                )
            }
        }

        if (points.size < 2) {
            // Single dot
            if (points.size == 1) {
                val x = padL + innerW / 2f
                val y = padT + (1f - points[0].value / 100f) * innerH
                drawCircle(dotFill, 5.dp.toPx(), Offset(x, y))
            }
            return@Canvas
        }

        // X positions
        val xs = points.mapIndexed { i, _ -> padL + i * innerW / (points.size - 1).toFloat() }
        val ys = points.map { padT + (1f - it.value / 100f) * innerH }

        // Background bars
        val barW = (innerW / points.size) * 0.6f
        xs.forEach { x ->
            drawRoundRect(
                color  = Color(0xFFFFE7B8),
                topLeft = Offset(x - barW / 2, padT),
                size   = Size(barW, innerH),
                cornerRadius = CornerRadius(4.dp.toPx()),
                alpha  = 0.5f
            )
        }

        // Fill area below line
        val fillPath = Path().apply {
            moveTo(xs[0], ys[0])
            for (i in 1 until points.size) {
                val cpX = (xs[i - 1] + xs[i]) / 2f
                cubicTo(cpX, ys[i - 1], cpX, ys[i], xs[i], ys[i])
            }
            lineTo(xs.last(), padT + innerH)
            lineTo(xs.first(), padT + innerH)
            close()
        }
        drawPath(
            fillPath,
            Brush.verticalGradient(
                listOf(orangeColor.copy(alpha = 0.25f), orangeColor.copy(alpha = 0f)),
                startY = padT,
                endY   = padT + innerH
            )
        )

        // Smooth bezier line
        val linePath = Path().apply {
            moveTo(xs[0], ys[0])
            for (i in 1 until points.size) {
                val cpX = (xs[i - 1] + xs[i]) / 2f
                cubicTo(cpX, ys[i - 1], cpX, ys[i], xs[i], ys[i])
            }
        }
        drawPath(
            linePath,
            color       = orangeColor,
            style       = Stroke(3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Dots
        xs.forEachIndexed { i, x ->
            drawCircle(dotFill, 4.dp.toPx(), Offset(x, ys[i]))
            drawCircle(Color.White, 2.dp.toPx(), Offset(x, ys[i]))
        }

        // Tooltip on best (max) point
        val bestIdx = points.indexOfFirst { it.value == points.maxOf { p -> p.value } }
        if (bestIdx >= 0) {
            val tx = xs[bestIdx]; val ty = ys[bestIdx]
            val tipW = 38.dp.toPx(); val tipH = 20.dp.toPx()
            val tipX = (tx - tipW / 2).coerceIn(padL, size.width - padR - tipW)
            drawRoundRect(
                color       = tooltipColor,
                topLeft     = Offset(tipX, ty - tipH - 6.dp.toPx()),
                size        = Size(tipW, tipH),
                cornerRadius = CornerRadius(10.dp.toPx())
            )
            // Tooltip tail
            val tailPath = Path().apply {
                moveTo(tx - 4.dp.toPx(), ty - 6.dp.toPx())
                lineTo(tx,               ty - 1.dp.toPx())
                lineTo(tx + 4.dp.toPx(), ty - 6.dp.toPx())
                close()
            }
            drawPath(tailPath, tooltipColor)
            // Tooltip text
            val tipPaint = NativePaint().apply {
                textSize  = 10.dp.toPx()
                typeface  = Typeface.DEFAULT_BOLD
                color     = android.graphics.Color.WHITE
                textAlign = NativePaint.Align.CENTER
                isAntiAlias = true
            }
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    "${points[bestIdx].value.toInt()}%",
                    tipX + tipW / 2,
                    ty - tipH / 2 - 6.dp.toPx() + 4.dp.toPx(),
                    tipPaint
                )
            }
        }

        // X labels
        val xLabelPaint = NativePaint().apply {
            textSize  = 9.dp.toPx()
            typeface  = Typeface.DEFAULT_BOLD
            color     = android.graphics.Color.argb(180, 138, 106, 79)
            textAlign = NativePaint.Align.CENTER
            isAntiAlias = true
        }
        points.forEachIndexed { i, point ->
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    point.label,
                    xs[i],
                    size.height - 4.dp.toPx(),
                    xLabelPaint
                )
            }
        }
    }
}

// ── Bottom nav (Langkah active) ────────────────────────────────────────────────

@Composable
private fun LangkahBottomBar(
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
                BottomTab(Icons.Filled.EmojiEvents, "Prestasi", false, onPrestasi)
                Spacer(Modifier.width(64.dp))
                BottomTab(Icons.Filled.BarChart,    "Langkah",  true,  onLangkah)
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
        Modifier.clickable(onClick = onClick).padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = label, tint = if (active) OrangeDeep else Muted, modifier = Modifier.size(22.dp))
        Text(label, fontSize = 10.sp, fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Bold, color = if (active) OrangeDeep else Muted)
    }
}
