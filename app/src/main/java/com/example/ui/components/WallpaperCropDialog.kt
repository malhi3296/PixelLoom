package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Wallpaper
import com.example.data.repository.WallpaperTarget
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightSurfaceBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodels.WallpaperViewModel

enum class CropAspectRatio(val label: String, val ratio: Float) {
    FREEFORM("Freeform", 0f),
    PHONE_SCREEN("Phone (9:20)", 9f / 20f),
    PORTRAIT("Portrait (9:16)", 9f / 16f),
    SQUARE("Square (1:1)", 1f),
    LANDSCAPE("Wide (16:9)", 16f / 9f)
}

enum class DragHandle {
    NONE,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    MOVE_BOX,
    PAN_IMAGE
}

@Composable
fun WallpaperCropDialog(
    wallpaper: Wallpaper,
    viewModel: WallpaperViewModel,
    onDismissRequest: () -> Unit,
    onApplyCrop: (Bitmap, WallpaperTarget) -> Unit,
    onApplyDirect: (WallpaperTarget) -> Unit
) {
    var sourceBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoadingBitmap by remember { mutableStateOf(true) }
    var selectedRatio by remember { mutableStateOf(CropAspectRatio.FREEFORM) }
    var showTargetSelector by remember { mutableStateOf(false) }
    var applyFullImageWithoutCrop by remember { mutableStateOf(false) }

    var userScale by remember { mutableFloatStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var rotationDegrees by remember { mutableIntStateOf(0) }

    // Finger drag of the entire crop box
    var boxMoveOffsetX by remember { mutableFloatStateOf(0f) }
    var boxMoveOffsetY by remember { mutableFloatStateOf(0f) }

    // Freeform manual crop adjustments
    var freeformLeftOffset by remember { mutableFloatStateOf(0f) }
    var freeformTopOffset by remember { mutableFloatStateOf(0f) }
    var freeformRightOffset by remember { mutableFloatStateOf(0f) }
    var freeformBottomOffset by remember { mutableFloatStateOf(0f) }

    var activeHandle by remember { mutableStateOf(DragHandle.NONE) }

    LaunchedEffect(wallpaper.fullUrl) {
        isLoadingBitmap = true
        val bmp = viewModel.loadBitmapForCropping(wallpaper.fullUrl)
        sourceBitmap = bmp
        isLoadingBitmap = false
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A))
                .testTag("wallpaper_crop_dialog")
        ) {
            if (isLoadingBitmap || sourceBitmap == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = BrandSecondary, strokeWidth = 3.dp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Preparing high-res wallpaper canvas...",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                val currentBitmap = remember(sourceBitmap, rotationDegrees) {
                    val src = sourceBitmap ?: return@remember null
                    if (rotationDegrees % 360 == 0) {
                        src
                    } else {
                        val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                        Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
                    }
                }

                if (currentBitmap != null) {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        val viewWidth = constraints.maxWidth.toFloat()
                        val viewHeight = constraints.maxHeight.toFloat()

                        // Calculate available viewport between top and bottom bars
                        val topInsetEstimate = 90f
                        val bottomInsetEstimate = 190f
                        val availableHeight = (viewHeight - topInsetEstimate - bottomInsetEstimate).coerceAtLeast(200f)

                        val maxCropWidth = viewWidth * 0.90f
                        val maxCropHeight = availableHeight * 0.94f

                        // Determine initial crop width & height based on aspect ratio
                        val initialCropWidth: Float
                        val initialCropHeight: Float

                        if (selectedRatio == CropAspectRatio.FREEFORM) {
                            val defaultRatio = 9f / 19.5f
                            initialCropHeight = maxCropHeight
                            initialCropWidth = (initialCropHeight * defaultRatio).coerceAtMost(maxCropWidth)
                        } else {
                            val targetRatio = selectedRatio.ratio
                            if (maxCropWidth / maxCropHeight > targetRatio) {
                                initialCropHeight = maxCropHeight
                                initialCropWidth = initialCropHeight * targetRatio
                            } else {
                                initialCropWidth = maxCropWidth
                                initialCropHeight = initialCropWidth / targetRatio
                            }
                        }

                        // Center crop box in the available vertical viewport (no excessive top spacing)
                        val viewportCenterY = topInsetEstimate + (availableHeight / 2f)
                        val baseLeft = (viewWidth - initialCropWidth) / 2f
                        val baseTop = viewportCenterY - (initialCropHeight / 2f)
                        val baseRight = baseLeft + initialCropWidth
                        val baseBottom = baseTop + initialCropHeight

                        // Apply box finger move and freeform corner deltas with boundary constraints
                        val finalCropLeft = (baseLeft + boxMoveOffsetX + freeformLeftOffset).coerceIn(12f, viewWidth - 80f)
                        val finalCropTop = (baseTop + boxMoveOffsetY + freeformTopOffset).coerceIn(topInsetEstimate, viewHeight - bottomInsetEstimate - 80f)
                        val finalCropRight = (baseRight + boxMoveOffsetX + freeformRightOffset).coerceIn(finalCropLeft + 80f, viewWidth - 12f)
                        val finalCropBottom = (baseBottom + boxMoveOffsetY + freeformBottomOffset).coerceIn(finalCropTop + 80f, viewHeight - bottomInsetEstimate)

                        val cropRect = Rect(
                            left = finalCropLeft,
                            top = finalCropTop,
                            right = finalCropRight,
                            bottom = finalCropBottom
                        )

                        // Main interactive viewport handling zoom, pan, and finger draggable crop box
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(selectedRatio, cropRect) {
                                    detectDragGestures(
                                        onDragStart = { startPos ->
                                            val touchThreshold = 65f
                                            activeHandle = when {
                                                selectedRatio == CropAspectRatio.FREEFORM && (startPos - cropRect.topLeft).getDistance() < touchThreshold -> DragHandle.TOP_LEFT
                                                selectedRatio == CropAspectRatio.FREEFORM && (startPos - cropRect.topRight).getDistance() < touchThreshold -> DragHandle.TOP_RIGHT
                                                selectedRatio == CropAspectRatio.FREEFORM && (startPos - cropRect.bottomLeft).getDistance() < touchThreshold -> DragHandle.BOTTOM_LEFT
                                                selectedRatio == CropAspectRatio.FREEFORM && (startPos - cropRect.bottomRight).getDistance() < touchThreshold -> DragHandle.BOTTOM_RIGHT
                                                cropRect.contains(startPos) -> DragHandle.MOVE_BOX
                                                else -> DragHandle.PAN_IMAGE
                                            }
                                        },
                                        onDragEnd = { activeHandle = DragHandle.NONE },
                                        onDragCancel = { activeHandle = DragHandle.NONE },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            when (activeHandle) {
                                                DragHandle.MOVE_BOX -> {
                                                    // Finger moves the crop box directly
                                                    boxMoveOffsetX += dragAmount.x
                                                    boxMoveOffsetY += dragAmount.y
                                                }
                                                DragHandle.PAN_IMAGE -> {
                                                    // Pan wallpaper image
                                                    panOffset = Offset(
                                                        x = panOffset.x + dragAmount.x,
                                                        y = panOffset.y + dragAmount.y
                                                    )
                                                }
                                                DragHandle.TOP_LEFT -> {
                                                    freeformLeftOffset += dragAmount.x
                                                    freeformTopOffset += dragAmount.y
                                                }
                                                DragHandle.TOP_RIGHT -> {
                                                    freeformRightOffset += dragAmount.x
                                                    freeformTopOffset += dragAmount.y
                                                }
                                                DragHandle.BOTTOM_LEFT -> {
                                                    freeformLeftOffset += dragAmount.x
                                                    freeformBottomOffset += dragAmount.y
                                                }
                                                DragHandle.BOTTOM_RIGHT -> {
                                                    freeformRightOffset += dragAmount.x
                                                    freeformBottomOffset += dragAmount.y
                                                }
                                                else -> {}
                                            }
                                        }
                                    )
                                }
                                .pointerInput(Unit) {
                                    detectTransformGestures { _, _, zoom, _ ->
                                        if (zoom != 1f) {
                                            userScale = (userScale * zoom).coerceIn(0.5f, 5.0f)
                                        }
                                    }
                                }
                        ) {
                            // Canvas drawing transformed image, mask, grid, and freeform handles
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val bmpW = currentBitmap.width.toFloat()
                                val bmpH = currentBitmap.height.toFloat()

                                val baseScale = maxOf(cropRect.width / bmpW, cropRect.height / bmpH)
                                val totalScale = baseScale * userScale

                                val drawW = bmpW * totalScale
                                val drawH = bmpH * totalScale

                                val imgLeft = (viewWidth / 2f) + panOffset.x - (drawW / 2f)
                                val imgTop = (cropRect.center.y) + panOffset.y - (drawH / 2f)

                                // Draw bitmap
                                drawImage(
                                    image = currentBitmap.asImageBitmap(),
                                    dstOffset = IntOffset(imgLeft.toInt(), imgTop.toInt()),
                                    dstSize = IntSize(drawW.toInt(), drawH.toInt())
                                )

                                // Darkened vignette outside crop window
                                val maskPath = Path().apply {
                                    fillType = PathFillType.EvenOdd
                                    addRect(Rect(0f, 0f, size.width, size.height))
                                    addRect(cropRect)
                                }
                                drawPath(maskPath, color = Color.Black.copy(alpha = 0.76f))

                                // Glowing border
                                drawRect(
                                    color = BrandSecondary,
                                    topLeft = cropRect.topLeft,
                                    size = cropRect.size,
                                    style = Stroke(width = 2.dp.toPx())
                                )

                                // Rule of thirds grid lines
                                val oneThirdW = cropRect.width / 3f
                                val twoThirdW = cropRect.width * 2f / 3f
                                val oneThirdH = cropRect.height / 3f
                                val twoThirdH = cropRect.height * 2f / 3f

                                val gridColor = Color.White.copy(alpha = 0.35f)
                                drawLine(gridColor, Offset(cropRect.left + oneThirdW, cropRect.top), Offset(cropRect.left + oneThirdW, cropRect.bottom), strokeWidth = 1.dp.toPx())
                                drawLine(gridColor, Offset(cropRect.left + twoThirdW, cropRect.top), Offset(cropRect.left + twoThirdW, cropRect.bottom), strokeWidth = 1.dp.toPx())
                                drawLine(gridColor, Offset(cropRect.left, cropRect.top + oneThirdH), Offset(cropRect.right, cropRect.top + oneThirdH), strokeWidth = 1.dp.toPx())
                                drawLine(gridColor, Offset(cropRect.left, cropRect.top + twoThirdH), Offset(cropRect.right, cropRect.top + twoThirdH), strokeWidth = 1.dp.toPx())

                                // Corner accent markers & handle knobs
                                val handleRadius = 8.dp.toPx()
                                val handleColor = BrandSecondary

                                // Draw circular handle dots on corners in Freeform mode
                                if (selectedRatio == CropAspectRatio.FREEFORM) {
                                    drawCircle(Color.White, radius = handleRadius + 2.dp.toPx(), center = cropRect.topLeft)
                                    drawCircle(handleColor, radius = handleRadius, center = cropRect.topLeft)

                                    drawCircle(Color.White, radius = handleRadius + 2.dp.toPx(), center = cropRect.topRight)
                                    drawCircle(handleColor, radius = handleRadius, center = cropRect.topRight)

                                    drawCircle(Color.White, radius = handleRadius + 2.dp.toPx(), center = cropRect.bottomLeft)
                                    drawCircle(handleColor, radius = handleRadius, center = cropRect.bottomLeft)

                                    drawCircle(Color.White, radius = handleRadius + 2.dp.toPx(), center = cropRect.bottomRight)
                                    drawCircle(handleColor, radius = handleRadius, center = cropRect.bottomRight)
                                } else {
                                    // Corner L-brackets for preset modes
                                    val cornerLen = 22.dp.toPx()
                                    val accent = BrandSecondary
                                    drawLine(accent, Offset(cropRect.left - 2, cropRect.top), Offset(cropRect.left + cornerLen, cropRect.top), strokeWidth = 4.dp.toPx())
                                    drawLine(accent, Offset(cropRect.left, cropRect.top - 2), Offset(cropRect.left, cropRect.top + cornerLen), strokeWidth = 4.dp.toPx())
                                    drawLine(accent, Offset(cropRect.right + 2, cropRect.top), Offset(cropRect.right - cornerLen, cropRect.top), strokeWidth = 4.dp.toPx())
                                    drawLine(accent, Offset(cropRect.right, cropRect.top - 2), Offset(cropRect.right, cropRect.top + cornerLen), strokeWidth = 4.dp.toPx())
                                    drawLine(accent, Offset(cropRect.left - 2, cropRect.bottom), Offset(cropRect.left + cornerLen, cropRect.bottom), strokeWidth = 4.dp.toPx())
                                    drawLine(accent, Offset(cropRect.left, cropRect.bottom + 2), Offset(cropRect.left, cropRect.bottom - cornerLen), strokeWidth = 4.dp.toPx())
                                    drawLine(accent, Offset(cropRect.right + 2, cropRect.bottom), Offset(cropRect.right - cornerLen, cropRect.bottom), strokeWidth = 4.dp.toPx())
                                    drawLine(accent, Offset(cropRect.right, cropRect.bottom + 2), Offset(cropRect.right, cropRect.bottom - cornerLen), strokeWidth = 4.dp.toPx())
                                }
                            }
                        }

                        // 1. Top Controls: ONLY Close and Undo (Reset) buttons as requested
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .statusBarsPadding()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = onDismissRequest,
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                                    .testTag("crop_dismiss_button")
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                            }

                            // Undo / Reset View Button
                            IconButton(
                                onClick = {
                                    userScale = 1f
                                    panOffset = Offset.Zero
                                    rotationDegrees = 0
                                    boxMoveOffsetX = 0f
                                    boxMoveOffsetY = 0f
                                    freeformLeftOffset = 0f
                                    freeformTopOffset = 0f
                                    freeformRightOffset = 0f
                                    freeformBottomOffset = 0f
                                },
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                                    .testTag("crop_reset_button")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Undo / Reset", tint = Color.White)
                            }
                        }

                        // 2. Small Overlay Button / Panel on Right Side with all cropping options
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF0F172A).copy(alpha = 0.88f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 12.dp)
                                .testTag("crop_aspect_ratio_overlay")
                        ) {
                            Column(
                                modifier = Modifier.padding(6.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CropAspectRatio.values().forEach { ratio ->
                                    val isSelected = selectedRatio == ratio
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) BrandSecondary else Color.Transparent,
                                        modifier = Modifier
                                            .size(width = 46.dp, height = 38.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                selectedRatio = ratio
                                                boxMoveOffsetX = 0f
                                                boxMoveOffsetY = 0f
                                                freeformLeftOffset = 0f
                                                freeformTopOffset = 0f
                                                freeformRightOffset = 0f
                                                freeformBottomOffset = 0f
                                            }
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            val labelText = when (ratio) {
                                                CropAspectRatio.FREEFORM -> "Free"
                                                CropAspectRatio.PHONE_SCREEN -> "9:20"
                                                CropAspectRatio.PORTRAIT -> "9:16"
                                                CropAspectRatio.SQUARE -> "1:1"
                                                CropAspectRatio.LANDSCAPE -> "16:9"
                                            }
                                            Text(
                                                text = labelText,
                                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.75f),
                                                fontSize = 11.5.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 3. Bottom Action Buttons: Guaranteed strictly above system navigation buttons
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xFF0F172A).copy(alpha = 0.95f))
                                    )
                                )
                                .navigationBarsPadding()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .widthIn(max = 500.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Full Image (Direct)
                                OutlinedButton(
                                    onClick = {
                                        applyFullImageWithoutCrop = true
                                        showTargetSelector = true
                                    },
                                    shape = RoundedCornerShape(16.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.45f)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                        .testTag("set_full_image_button")
                                ) {
                                    Icon(Icons.Default.FitScreen, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Full Image", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                                }

                                // Apply Crop
                                Button(
                                    onClick = {
                                        applyFullImageWithoutCrop = false
                                        showTargetSelector = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandSecondary),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .height(52.dp)
                                        .testTag("apply_crop_button")
                                ) {
                                    Icon(Icons.Default.Crop, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Apply Wallpaper",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Target Selector Bottom Sheet
                        if (showTargetSelector) {
                            SetWallpaperBottomSheet(
                                onDismissRequest = { showTargetSelector = false },
                                onOptionSelected = { target ->
                                    showTargetSelector = false
                                    if (applyFullImageWithoutCrop) {
                                        onApplyDirect(target)
                                    } else {
                                        val cropped = extractCroppedBitmap(
                                            source = currentBitmap,
                                            cropRect = cropRect,
                                            viewWidth = viewWidth,
                                            panOffset = panOffset,
                                            userScale = userScale
                                        )
                                        onApplyCrop(cropped, target)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Mathematically extracts the exact portion of the image enclosed in the crop frame
 */
private fun extractCroppedBitmap(
    source: Bitmap,
    cropRect: Rect,
    viewWidth: Float,
    panOffset: Offset,
    userScale: Float
): Bitmap {
    val bmpW = source.width.toFloat()
    val bmpH = source.height.toFloat()

    val baseScale = maxOf(cropRect.width / bmpW, cropRect.height / bmpH)
    val totalScale = baseScale * userScale

    val drawW = bmpW * totalScale
    val drawH = bmpH * totalScale

    val imgLeft = (viewWidth / 2f) + panOffset.x - (drawW / 2f)
    val imgTop = cropRect.center.y + panOffset.y - (drawH / 2f)

    // Calculate normalized crop bounds
    val normLeft = (cropRect.left - imgLeft) / drawW
    val normTop = (cropRect.top - imgTop) / drawH
    val normWidth = cropRect.width / drawW
    val normHeight = cropRect.height / drawH

    val cropX = (normLeft * bmpW).toInt().coerceIn(0, source.width - 1)
    val cropY = (normTop * bmpH).toInt().coerceIn(0, source.height - 1)
    val cropWidth = (normWidth * bmpW).toInt().coerceIn(1, source.width - cropX)
    val cropHeight = (normHeight * bmpH).toInt().coerceIn(1, source.height - cropY)

    return Bitmap.createBitmap(source, cropX, cropY, cropWidth, cropHeight)
}
