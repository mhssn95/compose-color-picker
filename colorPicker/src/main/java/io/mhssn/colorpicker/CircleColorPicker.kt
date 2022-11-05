package io.mhssn.colorpicker

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import kotlin.math.*

val colors = listOf(
    Color(0xffff0000),
    Color(0xffffff00),
    Color(0xff00ff00),
    Color(0xff00ffff),
    Color(0xff0000ff),
    Color(0xffff00ff),
    Color(0xffff0000),
)

enum class ColorRange {
    RedToYellow,
    YellowToGreen,
    GreenToCyan,
    CyanToBlue,
    BlueToPurple,
    PurpleToRed
}

@ExperimentalComposeUiApi
@Composable
fun CircleColorPicker(onPickedColor: (Color) -> Unit) {
    var pickerLocation by remember {
        mutableStateOf(Offset.Zero)
    }
    var pickerRadius by remember {
        mutableStateOf(0f)
    }
    var color by remember {
        mutableStateOf(Color.White)
    }
    LaunchedEffect(color) {
        onPickedColor(color)
    }
    Canvas(modifier = Modifier
        .size(200.dp)
        .onSizeChanged {
            pickerRadius = it.width.toFloat()/2
            pickerLocation = Offset(pickerRadius / 2, pickerRadius / 2)
        }
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val red: Int
                    val green: Int
                    val blue: Int
                    val angle = (Math.toDegrees(atan2(it.y - pickerRadius, it.x - pickerRadius).toDouble()) + 360) % 360
                    val length = getLength(it.x, it.y, pickerRadius)
                    val range: ColorRange
                    val t = angle/360f
                    val tt = t * 6 - when {
                        t < 1f/6 -> {
                            range = ColorRange.RedToYellow
                            0
                        }
                        t < 2f/6 -> {
                            range = ColorRange.YellowToGreen
                            1
                        }
                        t < 3f/6 -> {
                            range = ColorRange.GreenToCyan
                            2
                        }
                        t < 4f/6 -> {
                            range = ColorRange.CyanToBlue
                            3
                        }
                        t < 5f/6 -> {
                            range = ColorRange.BlueToPurple
                            4
                        }
                        else -> {
                            range = ColorRange.PurpleToRed
                            5
                        }
                    }
                    when(range) {
                        ColorRange.RedToYellow -> {
                            red = 255
                            green = (255f * tt).roundToInt()
                            blue = 0
                        }
                        ColorRange.YellowToGreen -> {
                            red = (255 * (1-tt)).roundToInt()
                            green = 255
                            blue = 0
                        }
                        ColorRange.GreenToCyan -> {
                            red = 0
                            green = 255
                            blue = (255 * tt).roundToInt()
                        }
                        ColorRange.CyanToBlue -> {
                            red = 0
                            green = (255 * (1-tt)).roundToInt()
                            blue = 255
                        }
                        ColorRange.BlueToPurple -> {
                            red = (255 * tt).roundToInt()
                            green = 0
                            blue = 255
                        }
                        ColorRange.PurpleToRed -> {
                            red = 255
                            green = 0
                            blue = (255 * (1-tt)).roundToInt()
                        }
                    }
                    pickerLocation = getBoundedPointWithinRadius(it.x, it.y, length, pickerRadius)
                    color = Color(red, green ,blue)
                }
            }
            return@pointerInteropFilter true
        }) {
        drawCircle(
            Brush.sweepGradient(colors)
        )
        drawCircle(
            ShaderBrush(
                RadialGradientShader(
                    Offset(size.width / 2f, size.height / 2f),
                    colors = listOf(Color.White, Color.Transparent),
                    radius = size.width / 2f
                )
            )
        )
        drawColorSelector(color, pickerLocation)
    }
}

private fun getLength(x: Float, y: Float, radius: Float): Float {
    return sqrt((x - radius)*(x - radius)+(y - radius)*(y - radius))
}

private fun getBoundedPointWithinRadius(x: Float, y: Float, length: Float, radius: Float): Offset {
    return if (length > radius) {
        val angle = atan2(y - radius, x - radius)
        Offset(x - (length - radius) * cos(angle), y -(length - radius) * sin(angle))
    } else {
        Offset(x, y)
    }
}