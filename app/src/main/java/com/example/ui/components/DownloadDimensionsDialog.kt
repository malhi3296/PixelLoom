package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Wallpaper
import com.example.data.model.WallpaperDimension
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class DimensionMode {
    ORIGINAL,
    PRESET,
    CUSTOM
}

@Composable
fun DownloadDimensionsDialog(
    wallpaper: Wallpaper,
    onDismissRequest: () -> Unit,
    onDownloadOriginal: () -> Unit,
    onDownloadCustom: (width: Int, height: Int) -> Unit,
    isDownloading: Boolean = false
) {
    var mode by remember { mutableStateOf(DimensionMode.ORIGINAL) }

    val presets = remember {
        listOf(
            WallpaperDimension("FHD+ (Modern Phone)", 1080, 2400),
            WallpaperDimension("QHD+ (Flagship Display)", 1440, 3200),
            WallpaperDimension("Classic Full HD", 1080, 1920),
            WallpaperDimension("4K Ultra HD (3840p)", 2160, 3840),
            WallpaperDimension("Super Retina (iPhone Aspect)", 1170, 2532)
        )
    }

    var selectedPreset by remember { mutableStateOf(presets[0]) }
    var customWidth by remember { mutableStateOf(wallpaper.width.toString()) }
    var customHeight by remember { mutableStateOf(wallpaper.height.toString()) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("download_dimensions_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Download Wallpaper",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Select dimensions or specify custom scaling:",
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mode 1: Original Dimensions
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { mode = DimensionMode.ORIGINAL }
                        .border(
                            width = if (mode == DimensionMode.ORIGINAL) 1.5.dp else 1.dp,
                            color = if (mode == DimensionMode.ORIGINAL) NeonCyan else DarkSurfaceBorder,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    color = if (mode == DimensionMode.ORIGINAL) DarkSurfaceElevated else DarkSurface
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = mode == DimensionMode.ORIGINAL,
                            onClick = { mode = DimensionMode.ORIGINAL },
                            colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Original Dimensions",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Full resolution (${wallpaper.width} × ${wallpaper.height} px)",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Mode 2: Preset Common Device Resolutions
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { mode = DimensionMode.PRESET }
                        .border(
                            width = if (mode == DimensionMode.PRESET) 1.5.dp else 1.dp,
                            color = if (mode == DimensionMode.PRESET) NeonCyan else DarkSurfaceBorder,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    color = if (mode == DimensionMode.PRESET) DarkSurfaceElevated else DarkSurface
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = mode == DimensionMode.PRESET,
                                onClick = { mode = DimensionMode.PRESET },
                                colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Preset Device Resolutions",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (mode == DimensionMode.PRESET) {
                            Spacer(modifier = Modifier.height(10.dp))
                            presets.forEach { preset ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp, horizontal = 8.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { selectedPreset = preset }
                                        .background(if (selectedPreset == preset) ElectricViolet.copy(alpha = 0.2f) else Color.Transparent)
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = preset.label, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                        Text(text = preset.resolutionLabel, color = NeonCyan, fontSize = 11.sp)
                                    }
                                    if (selectedPreset == preset) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Mode 3: Custom Dimensions
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { mode = DimensionMode.CUSTOM }
                        .border(
                            width = if (mode == DimensionMode.CUSTOM) 1.5.dp else 1.dp,
                            color = if (mode == DimensionMode.CUSTOM) NeonCyan else DarkSurfaceBorder,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    color = if (mode == DimensionMode.CUSTOM) DarkSurfaceElevated else DarkSurface
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = mode == DimensionMode.CUSTOM,
                                onClick = { mode = DimensionMode.CUSTOM },
                                colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Custom Dimensions",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (mode == DimensionMode.CUSTOM) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = customWidth,
                                    onValueChange = { customWidth = it.filter { ch -> ch.isDigit() } },
                                    label = { Text("Width (px)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f).testTag("custom_width_input"),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = NeonCyan,
                                        unfocusedBorderColor = DarkSurfaceBorder,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )

                                OutlinedTextField(
                                    value = customHeight,
                                    onValueChange = { customHeight = it.filter { ch -> ch.isDigit() } },
                                    label = { Text("Height (px)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f).testTag("custom_height_input"),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = NeonCyan,
                                        unfocusedBorderColor = DarkSurfaceBorder,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        enabled = !isDownloading
                    ) {
                        Text("Cancel", color = TextSecondary)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            when (mode) {
                                DimensionMode.ORIGINAL -> onDownloadOriginal()
                                DimensionMode.PRESET -> onDownloadCustom(selectedPreset.width, selectedPreset.height)
                                DimensionMode.CUSTOM -> {
                                    val w = customWidth.toIntOrNull() ?: wallpaper.width
                                    val h = customHeight.toIntOrNull() ?: wallpaper.height
                                    onDownloadCustom(w, h)
                                }
                            }
                        },
                        enabled = !isDownloading,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        modifier = Modifier.testTag("confirm_download_button")
                    ) {
                        if (isDownloading) {
                            CircularProgressIndicator(
                                color = DarkSurface,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Saving...", color = DarkSurface, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Download, contentDescription = null, tint = DarkSurface, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Download", color = DarkSurface, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
