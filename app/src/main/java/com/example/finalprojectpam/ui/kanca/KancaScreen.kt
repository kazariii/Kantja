package com.example.finalprojectpam.ui.kanca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

private val BgCream    = Color(0xFFFBF3E0)
private val Orange     = Color(0xFFF5A623)
private val OrangeDeep = Color(0xFFED8A0F)
private val OrangeLight = Color(0xFFFFC078)
private val Brown      = Color(0xFF5C3A1E)
private val Brown2     = Color(0xFF7A4A2A)
private val TextSoft   = Color(0xFF8A6A4F)

@Composable
fun KancaScreen(
    navController: NavController,
    viewModel: KancaViewModel = viewModel()
) {
    val messages   by viewModel.messages.collectAsState()
    val isLoading  by viewModel.isLoading.collectAsState()
    val listState  = rememberLazyListState()
    val scope      = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scope.launch { listState.animateScrollToItem(messages.lastIndex) }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(BgCream)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        KancaHeader(onBack = { navController.popBackStack() })

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(message = msg)
            }
            if (isLoading) {
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        TypingIndicator()
                    }
                }
            }
        }

        QuickReplyRow(
            replies = QuickReply.entries,
            onReply = { viewModel.onQuickReply(it) }
        )
    }
}

@Composable
private fun KancaHeader(onBack: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Box(
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFE7B8))
                .clickable(onClick = onBack)
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Kembali",
                tint = OrangeDeep,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "KANCA",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                style = LocalTextStyle.current.copy(
                    brush = Brush.verticalGradient(listOf(Color(0xFFFFB84D), OrangeDeep))
                )
            )
            Text(
                text = "Asisten Orang Tua",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextSoft
            )
        }

        Box(
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFE7B8))
                .align(Alignment.CenterEnd),
            contentAlignment = Alignment.Center
        ) {
            Text("🐯", fontSize = 18.sp)
        }
    }
    HorizontalDivider(color = Color(0xFFF0E1C4), thickness = 1.dp)
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val isKanca = !message.isFromUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isKanca) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        if (isKanca) {
            Box(
                Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFE7B8)),
                contentAlignment = Alignment.Center
            ) {
                Text("🐯", fontSize = 14.sp)
            }
            Spacer(Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isKanca) Alignment.Start else Alignment.End,
            modifier = Modifier.widthIn(max = 260.dp)
        ) {
            Box(
                Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (isKanca) 4.dp else 18.dp,
                            bottomEnd = if (isKanca) 18.dp else 4.dp
                        )
                    )
                    .then(
                        if (isKanca)
                            Modifier.background(
                                Brush.linearGradient(listOf(OrangeLight, Orange))
                            )
                        else
                            Modifier.background(Brown2)
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    lineHeight = 20.sp
                )
            }
            Spacer(Modifier.height(3.dp))
            Text(
                text = message.time,
                fontSize = 10.sp,
                color = TextSoft,
                fontWeight = FontWeight.Bold
            )
        }

        if (!isKanca) {
            Spacer(Modifier.width(8.dp))
        }
    }
}

@Composable
private fun TypingIndicator() {
    Row(verticalAlignment = Alignment.Bottom) {
        Box(
            Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFE7B8)),
            contentAlignment = Alignment.Center
        ) {
            Text("🐯", fontSize = 14.sp)
        }
        Spacer(Modifier.width(8.dp))
        Box(
            Modifier
                .clip(RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp))
                .background(Brush.linearGradient(listOf(OrangeLight, Orange)))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text("• • •", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun QuickReplyRow(
    replies: List<QuickReply>,
    onReply: (QuickReply) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        HorizontalDivider(color = Color(0xFFF0E1C4), thickness = 1.dp)
        Text(
            text = "Tanya KANCA",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextSoft,
            modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 4.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(replies) { reply ->
                QuickReplyChip(reply = reply, onClick = { onReply(reply) })
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
private fun QuickReplyChip(reply: QuickReply, onClick: () -> Unit) {
    val emoji = when (reply) {
        QuickReply.TARGET       -> "🎯"
        QuickReply.PERKEMBANGAN -> "📊"
        QuickReply.REKOMENDASI  -> "📚"
    }

    Surface(
        modifier = Modifier
            .width(140.dp)
            .shadow(3.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFE7B8)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 14.sp)
                }
                Spacer(Modifier.width(6.dp))
                Text(
                    text = reply.title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Brown,
                    lineHeight = 14.sp
                )
            }
            Text(
                text = reply.subtitle,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextSoft,
                lineHeight = 13.sp
            )
        }
    }
}
