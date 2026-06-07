package com.example.finalprojectpam.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.finalprojectpam.ui.navigation.Screen

// ── Design tokens ─────────────────────────────────────────────────────────────
private val BgCream     = Color(0xFFFBF3E0)
private val Orange      = Color(0xFFF5A623)
private val OrangeLight = Color(0xFFFFF4E0)
private val Brown       = Color(0xFF5C3A1E)
private val Brown2      = Color(0xFF7A4A2A)
private val TextSoft    = Color(0xFF8A6A4F)

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var tab by remember { mutableStateOf("login") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
            viewModel.resetState()
        }
    }
    LaunchedEffect(tab) { viewModel.resetState() }

    Box(
        Modifier
            .fillMaxSize()
            .background(BgCream)
    ) {
        // Sparkle decorations
        Sparkle(Modifier.offset(x = 28.dp, y = 80.dp), 20.dp, Color(0xFFFFB84D))
        Sparkle(
            Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-36).dp, y = 116.dp),
            14.dp, Color(0xFFFFCB6E)
        )

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(50.dp))

            // Kimo mascot with bobbing animation
            KimoMascot(Modifier.size(120.dp))

            Spacer(Modifier.height(12.dp))

            // Title & subtitle
            Text(
                text = if (tab == "login") "Selamat Datang Kembali!" else "Yuk, Mulai Petualangan",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Brown,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (tab == "login")
                    "Masuk untuk lanjut bermain bersama Kimo"
                else
                    "Buat akun gratis dan ajak Kimo bercerita",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSoft,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(Modifier.height(20.dp))

            // Tab switcher: Masuk / Daftar
            Row(
                Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(OrangeLight)
                    .padding(4.dp)
            ) {
                listOf("login" to "Masuk", "register" to "Daftar").forEach { (id, label) ->
                    Box(
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (tab == id) Orange else Color.Transparent)
                            .clickable { tab = id }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = if (tab == id) Color.White else Brown2
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // Form fields
            Column(
                Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                if (tab == "register") {
                    InputCard(
                        label = "Nama Anak",
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Aira Putri"
                    )
                    Spacer(Modifier.height(12.dp))
                }

                InputCard(
                    label = "Email Orang Tua",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "orangtua@email.com",
                    keyboardType = KeyboardType.Email
                )
                Spacer(Modifier.height(12.dp))

                InputCard(
                    label = "Kata Sandi",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "••••••••",
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onTogglePassword = { passwordVisible = !passwordVisible }
                )

                Spacer(Modifier.height(8.dp))

                if (authState is AuthState.Error) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                }

                // Primary button
                Button(
                    onClick = {
                        if (tab == "login") viewModel.login(email, password)
                        else viewModel.register(name, email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    enabled = authState !is AuthState.Loading
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = if (tab == "login") "Masuk" else "Buat Akun",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

// ── Reusable composables ───────────────────────────────────────────────────────

@Composable
private fun InputCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    Surface(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            Modifier.padding(start = 16.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextSoft,
                    modifier = Modifier.padding(top = 10.dp)
                )
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Brown
                    ),
                    visualTransformation = if (isPassword && !passwordVisible)
                        PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    modifier = Modifier.padding(bottom = 12.dp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD4B896)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
            if (isPassword && onTogglePassword != null) {
                IconButton(onClick = onTogglePassword) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = TextSoft,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

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
private fun KimoMascot(modifier: Modifier) {
    val inf = rememberInfiniteTransition(label = "bob")
    val dy by inf.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "dy"
    )

    Canvas(modifier.offset(y = dy.dp)) {
        val w = size.width
        val cx = w / 2f
        val cy = size.height * 0.52f
        val r = w * 0.36f

        // Ears
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

        // Head (radial gradient)
        drawCircle(
            Brush.radialGradient(
                listOf(Color(0xFFFFC078), Color(0xFFF5A623), Color(0xFFD9881A)),
                Offset(cx, cy - r * .2f), r * 1.3f
            ),
            r, Offset(cx, cy)
        )
        drawCircle(Color(0xFF7A4A2A), r, Offset(cx, cy), style = Stroke(w * .03f))

        // Muzzle
        drawOval(Color(0xFFFFF1D8), Offset(cx - r * .58f, cy - r * .18f), Size(r * 1.16f, r * .86f))

        // Cheeks
        drawOval(Color(0xFFFFB1A0).copy(alpha = .7f), Offset(cx - r * .92f, cy - r * .08f), Size(r * .36f, r * .24f))
        drawOval(Color(0xFFFFB1A0).copy(alpha = .7f), Offset(cx + r * .56f, cy - r * .08f), Size(r * .36f, r * .24f))

        // Eyes
        drawOval(Color(0xFF3A2410), Offset(cx - r * .46f, cy - r * .42f), Size(r * .28f, r * .34f))
        drawCircle(Color.White, r * .07f, Offset(cx - r * .38f, cy - r * .50f))
        drawOval(Color(0xFF3A2410), Offset(cx + r * .18f, cy - r * .42f), Size(r * .28f, r * .34f))
        drawCircle(Color.White, r * .07f, Offset(cx + r * .26f, cy - r * .50f))

        // Nose
        drawOval(Color(0xFF3A2410), Offset(cx - r * .10f, cy + r * .04f), Size(r * .20f, r * .14f))

        // Smile
        drawPath(
            Path().apply {
                moveTo(cx - r * .26f, cy + r * .28f)
                quadraticTo(cx, cy + r * .52f, cx + r * .26f, cy + r * .28f)
            },
            Color(0xFF3A2410),
            style = Stroke(w * .03f, cap = StrokeCap.Round)
        )
    }
}
