package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

@Composable
fun BrandLogoHeader(
    modifier: Modifier = Modifier,
    iconSize: Dp = 36.dp,
    titleSize: TextUnit = 19.sp,
    showTagline: Boolean = false,
    isDark: Boolean = isSystemInDarkTheme()
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo Container with vibrant purple badge & white cutout
        Box(
            modifier = Modifier
                .size(iconSize)
                .shadow(
                    elevation = if (isDark) 4.dp else 3.dp,
                    shape = RoundedCornerShape(10.dp),
                    ambientColor = Color(0xFF6B38FB).copy(alpha = 0.3f),
                    spotColor = Color(0xFF6B38FB).copy(alpha = 0.4f)
                )
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF6B38FB))
                .border(
                    width = 0.75.dp,
                    color = Color.White.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pixelloom_logo_white),
                contentDescription = "App Logo",
                modifier = Modifier.size(iconSize - 10.dp)
            )
        }
    }
}
