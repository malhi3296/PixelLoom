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
        Brush.verticalGradient(listOf(Color(0xFF090D16), Color(0xFF0F172A)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFF8FAFC), Color(0xFFEEF2FF)))
    }

    val cardBg = if (isDark) Color(0xFF1E293B) else Color.White
    val cardBorder = if (isDark) BrandSecondary.copy(alpha = 0.35f) else LightSurfaceBorder
    val titleTextColor = if (isDark) Color.White else Color(0xFF0F172A)
    val loomColor = if (isDark) BrandSecondary else BrandPrimary

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
                .size(240.dp)
                .scale(pulseScale)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            if (isDark) BrandSecondary.copy(alpha = 0.28f) else BrandPrimary.copy(alpha = 0.14f),
                            if (isDark) BrandPrimary.copy(alpha = 0.15f) else BrandSecondary.copy(alpha = 0.06f),
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
            // Animated Logo Emblem
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .scale(scaleAnim.value)
                    .alpha(alphaAnim.value)
                    .shadow(
                        elevation = if (isDark) 8.dp else 10.dp,
                        shape = RoundedCornerShape(26.dp),
                        spotColor = if (isDark) BrandSecondary.copy(alpha = 0.4f) else Color(0x244F46E5),
                        ambientColor = if (isDark) BrandPrimary.copy(alpha = 0.2f) else Color(0x18000000)
                    )
                    .clip(RoundedCornerShape(26.dp))
                    .background(cardBg)
                    .border(
                        width = 1.5.dp,
                        color = cardBorder,
                        shape = RoundedCornerShape(26.dp)
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pixelloom_logo),
                    contentDescription = "PixelLoom Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Animated App Name Typography
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlphaAnim.value)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = titleTextColor, fontWeight = FontWeight.Black)) {
                            append("Pixel")
                        }
                        withStyle(style = SpanStyle(color = loomColor, fontWeight = FontWeight.ExtraBold)) {
                            append("Loom")
                        }
                    },
                    fontSize = 32.sp,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "CURATED HIGH-RES WALLPAPERS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Community pill badge and pulsing indicators
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(badgeAlphaAnim.value)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDark) Color(0xFF1E293B) else Color.White,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDark) Color(0xFF334155) else LightSurfaceBorder
                    ),
                    shadowElevation = if (isDark) 0.dp else 1.dp
                ) {
                    Text(
                        text = "✨ 24+ Creative Communities",
                        color = loomColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Weaving dot indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(BrandSecondary))
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(BrandPrimary))
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(BrandSecondary.copy(alpha = 0.5f)))
                }
            }
        }
    }
}
