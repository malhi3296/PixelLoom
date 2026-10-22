package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.LightSurfaceBorder
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    isDark: Boolean = isSystemInDarkTheme()
) {
    val scaleAnim = remember { Animatable(0.75f) }
    val alphaAnim = remember { Animatable(0f) }
    val textAlphaAnim = remember { Animatable(0f) }
    val badgeAlphaAnim = remember { Animatable(0f) }

    // Breathing glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    LaunchedEffect(Unit) {
        // Step 1: Scale and fade logo in
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 350)
        )
        // Step 2: Fade title in
        textAlphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400)
        )
        // Step 3: Fade badge in
        badgeAlphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 350)
        )

        // Snappy splash duration for fast loading
        delay(750)
        onSplashFinished()
    }

    // Color definitions for Light and Dark modes
    val bgBrush = if (isDark) {
        Brush.verticalGradient(
            listOf(
                Color(0xFF090A12),
                Color(0xFF110E1F),
                Color(0xFF161226)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                Color(0xFFFFFFFF),
                Color(0xFFFAF7FF),
                Color(0xFFF3ECFE)
            )
        )
    }

    // Logo squircle badge with gradient purple matching app icon
    val badgeBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF7C3AED), Color(0xFF6B38FB), Color(0xFF5827E8))
    )
    val titleTextColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subtitleColor = if (isDark) Color(0xFFA5B4FC) else Color(0xFF6B38FB)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Ambient radial glow behind logo
        Box(
            modifier = Modifier
                .size(280.dp)
                .scale(pulseScale)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            if (isDark) Color(0xFF7C3AED).copy(alpha = 0.35f) else Color(0xFF6B38FB).copy(alpha = 0.18f),
                            if (isDark) Color(0xFF4C1D95).copy(alpha = 0.18f) else Color(0xFFA855F7).copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Logo Emblem - Purple background with crisp white cutout in front
            Box(
                modifier = Modifier
                    .size(118.dp)
                    .scale(scaleAnim.value)
                    .alpha(alphaAnim.value)
                    .shadow(
                        elevation = if (isDark) 16.dp else 12.dp,
                        shape = RoundedCornerShape(28.dp),
                        spotColor = Color(0xFF6B38FB).copy(alpha = if (isDark) 0.6f else 0.4f),
                        ambientColor = Color(0xFF7C3AED).copy(alpha = 0.25f)
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .background(badgeBrush)
                    .border(
                        width = 1.5.dp,
                        color = Color.White.copy(alpha = if (isDark) 0.22f else 0.35f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pixelloom_logo_white),
                    contentDescription = "PixelLoom Logo Cutout",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Animated App Name Typography - perfectly balanced together
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlphaAnim.value)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Pixel",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp,
                        color = titleTextColor
                    )
                    Text(
                        text = "Loom",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp,
                        color = Color(0xFF7C3AED)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "CURATED HIGH-RES WALLPAPERS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.2.sp,
                    color = subtitleColor
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Multi-Color Palette Dots & Community Badge
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(badgeAlphaAnim.value)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isDark) Color(0xFF1E1A33) else Color.White,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDark) Color(0xFF3B3363) else Color(0xFFE2D9FC)
                    ),
                    shadowElevation = if (isDark) 0.dp else 2.dp
                ) {
                    Text(
                        text = "✦ 24+ Creative Communities",
                        color = if (isDark) Color(0xFFC4B5FD) else Color(0xFF6B38FB),
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Multi-color spectrum indicator dots reflecting logo colorways
                Row(
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF7C3AED))) // Purple
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF06B6D4))) // Cyan
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFF97316))) // Sunset Orange
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF10B981))) // Emerald Mint
                }
            }
        }
    }
}
