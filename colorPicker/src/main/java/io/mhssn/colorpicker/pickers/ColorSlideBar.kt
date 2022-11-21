package io.mhssn.colorpicker.pickers

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.mhssn.colorpicker.data.Colors
import io.mhssn.colorpicker.ext.drawTransparentBackground

private const val thumbRadius = 20f

@ExperimentalComposeUiApi
@Composable
internal fun ColorSlideBar(colors: List<Color>, onProgress: (Float) -> Unit) {
    var progress by remember {
        mutableStateOf(1f)
    }
    var slideBarSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    LaunchedEffect(progress) {
        onProgress(progress)
    }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(22.dp)
            .onSizeChanged {
                slideBarSize = it
            }
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                        progress = (it.x / slideBarSize.width).coerceIn(0f, 1f)
                    }
                }
                return@pointerInteropFilter true
            }
            .clipToBounds()
            .clip(RoundedCornerShape(100))
            .border(0.2.dp, Color.LightGray, RoundedCornerShape(100))
    ) {
        drawTransparentBackground(3)
        drawRect(Brush.horizontalGradient(colors, startX = size.height/2, endX = size.width - size.height/2))
        drawCircle(
            Color.White,
            radius = thumbRadius,
            center = Offset(
                thumbRadius + (size.height / 2 - thumbRadius) + ((size.width - (thumbRadius + (size.height / 2 - thumbRadius)) * 2) * progress),
                size.height / 2
            )
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
private fun ColorSlideBarPreview() {
    ColorSlideBar(Colors.gradientColors) {}
}