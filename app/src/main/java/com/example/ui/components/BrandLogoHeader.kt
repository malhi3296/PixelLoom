package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.LightSurfaceBorder

@Composable
fun BrandLogoHeader(
    modifier: Modifier = Modifier,
    iconSize: Dp = 36.dp,
    titleSize: TextUnit = 19.sp,
    showTagline: Boolean = true,
    isDark: Boolean = isSystemInDarkTheme()
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo Container with vibrant purple badge & white cutout (matching app icon)
        Box(
            modifier = Modifier
                .size(iconSize)
                .shadow(
                    elevation = if (isDark) 4.dp else 3.dp,
                    shape = RoundedCornerShape(9.dp),
                    ambientColor = Color(0xFF6B38FB).copy(alpha = 0.3f),
                    spotColor = Color(0xFF6B38FB).copy(alpha = 0.4f)
                )
                .clip(RoundedCornerShape(9.dp))
                .background(Color(0xFF6B38FB))
                .border(
                    width = 0.75.dp,
                    color = Color.White.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(9.dp)
                )
                .padding(4.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pixelloom_logo_white),
                contentDescription = "PixelLoom Logo",
                modifier = Modifier.size(iconSize - 9.dp)
            )
        }

        Spacer(modifier = Modifier.width(9.dp))

        // Unified Brand Typography
        Column {
            val titleTextColor = if (isDark) Color.White else Color(0xFF0F172A)
            val loomColor = if (isDark) BrandSecondary else BrandPrimary

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = titleTextColor, fontWeight = FontWeight.Black)) {
                        append("Pixel")
                    }
                    withStyle(style = SpanStyle(color = loomColor, fontWeight = FontWeight.ExtraBold)) {
                        append("Loom")
                    }
                },
                fontSize = titleSize,
                letterSpacing = (-0.3).sp,
                lineHeight = titleSize
            )

            if (showTagline) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(loomColor)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "CURATED WALLPAPERS",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                    )
                }
            }
        }
    }
}
