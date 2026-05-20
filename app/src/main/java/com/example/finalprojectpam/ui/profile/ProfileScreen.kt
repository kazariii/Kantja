package com.example.finalprojectpam.ui.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalprojectpam.ui.navigation.Screen

// ── Design tokens ──────────────────────────────────────────────────────────────
private val BgCream    = Color(0xFFFBF3E0)
private val Orange     = Color(0xFFF5A623)
private val OrangeDeep = Color(0xFFED8A0F)
private val Brown      = Color(0xFF5C3A1E)
private val Brown2     = Color(0xFF7A4A2A)
private val TextSoft   = Color(0xFF8A6A4F)
private val Muted      = Color(0xFFB89A7C)
private val Divider    = Color(0xFFF0E1C4)

// ── Main screen ────────────────────────────────────────────────────────────────

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    val userProfile    by viewModel.userProfile.collectAsState()
    val isLoggedOut    by viewModel.isLoggedOut.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    // Navigasi ke Login setelah logout
    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val totalXp  = userProfile?.totalScore ?: 0
    val level    = totalXp / 100 + 1
    val stories  = userProfile?.storiesCompleted ?: 0
    val name     = userProfile?.name?.ifBlank { "Pengguna" } ?: "Pengguna"

    Scaffold(
        containerColor = BgCream,
        bottomBar = {
            ProfilBottomBar(
                onBeranda  = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Home.route) { inclusive = true } } },
                onPrestasi = { navController.navigate(Screen.Score.route) },
                onLangkah  = {},
                onProfil   = {}
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Header ─────────────────────────────────────────────────────
            Text(
                "Profil",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Brown
            )

            Spacer(Modifier.height(14.dp))

            // ── Profile card ───────────────────────────────────────────────
            Surface(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                shadowElevation = 3.dp
            ) {
                Row(
                    Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        Modifier
                            .size(76.dp)
                            .shadow(3.dp, CircleShape, spotColor = OrangeDeep)
                            .background(Color(0xFFFFD9A8), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🐯", fontSize = 38.sp)
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Brown
                        )
                        Text(
                            "Level $level · $totalXp XP",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSoft
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Chip(
                                label = "🔥 $stories cerita",
                                bgColor = Color(0xFFFFE7B8),
                                textColor = Brown
                            )
                            Chip(
                                label = "🌟 $stories badge",
                                bgColor = Color(0xFFFBD5DB),
                                textColor = Color(0xFFA85C6E)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Pengaturan ─────────────────────────────────────────────────
            Text(
                "Pengaturan",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Brown
            )

            Spacer(Modifier.height(10.dp))

            Surface(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column {
                    SettingsRow(
                        icon = "👤",
                        label = "Akun & Profil",
                        subtitle = "Edit nama kamu",
                        showDivider = true,
                        onClick = { showEditDialog = true }
                    )
                    SettingsRow(
                        icon = "🔔",
                        label = "Notifikasi",
                        subtitle = "Pengingat baca harian",
                        showDivider = true,
                        onClick = {}
                    )
                    SettingsRow(
                        icon = "🎵",
                        label = "Suara & Musik",
                        subtitle = "Kontrol volume narasi",
                        showDivider = false,
                        onClick = {}
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Mode Orang Tua ─────────────────────────────────────────────
            Text(
                "Mode Orang Tua",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Brown
            )

            Spacer(Modifier.height(10.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(22.dp))
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFFFFE7B8), Color(0xFFFFD18E)))
                    )
                    .border(2.dp, Color(0xFFFFC078), RoundedCornerShape(22.dp))
                    .clickable { /* KANCA coming soon */ }
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(50.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("💬", fontSize = 22.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Bicara dengan KANCA",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Brown
                        )
                        Text(
                            "Ringkasan aktivitas anak harian",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Brown2
                        )
                    }
                    Text("›", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = OrangeDeep)
                }
            }

            Spacer(Modifier.height(18.dp))

            // ── Keluar ─────────────────────────────────────────────────────
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFBDADF))
                    .clickable { viewModel.logout() }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Keluar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFA85C6E)
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    // ── Dialog edit nama ───────────────────────────────────────────────────────
    if (showEditDialog) {
        EditNameDialog(
            currentName = userProfile?.name ?: "",
            onConfirm = { newName ->
                viewModel.createOrUpdateProfile(newName)
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }
}

// ── Composables ────────────────────────────────────────────────────────────────

@Composable
private fun Chip(label: String, bgColor: Color, textColor: Color) {
    Box(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = textColor)
    }
}

@Composable
private fun SettingsRow(
    icon: String,
    label: String,
    subtitle: String,
    showDivider: Boolean,
    onClick: () -> Unit
) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFE7B8)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 18.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(label, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Brown)
                Text(subtitle, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSoft)
            }
            Text("›", fontSize = 18.sp, color = Muted)
        }
        if (showDivider) {
            HorizontalDivider(
                Modifier.padding(horizontal = 16.dp),
                color = Divider,
                thickness = 1.dp
            )
        }
    }
}

@Composable
private fun EditNameDialog(
    currentName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var nameInput by remember { mutableStateOf(currentName) }
    val keyboard = LocalSoftwareKeyboardController.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(22.dp),
        title = {
            Text("Edit Nama", fontWeight = FontWeight.ExtraBold, color = Brown)
        },
        text = {
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text("Nama kamu") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Box(
                Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Orange)
                    .clickable {
                        if (nameInput.isNotBlank()) onConfirm(nameInput.trim())
                    }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text("Simpan", fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = TextSoft, fontWeight = FontWeight.Bold)
            }
        }
    )
}

// ── Bottom nav (Profil active) ─────────────────────────────────────────────────

@Composable
private fun ProfilBottomBar(
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
                BottomTab(Icons.Filled.BarChart,    "Langkah",  false, onLangkah)
                BottomTab(Icons.Filled.Person,      "Profil",   true,  onProfil)
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
