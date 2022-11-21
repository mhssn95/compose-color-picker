package io.mhssn.colorpicker.pickers

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.mhssn.colorpicker.data.ColorRange
import io.mhssn.colorpicker.data.Colors.gradientColors
import io.mhssn.colorpicker.ext.*
import io.mhssn.colorpicker.helper.BoundedPointStrategy
import io.mhssn.colorpicker.helper.ColorPickerHelper
import io.mhssn.colorpicker.helper.MathHelper.getBoundedPointWithInRadius
import io.mhssn.colorpicker.helper.MathHelper.getLength
import kotlin.math.atan2
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
@Composable
internal fun CircleColorPicker(
    modifier: Modifier = Modifier,
    showAlphaBar: Boolean,
    showBrightnessBar: Boolean,
    lightCenter: Boolean,
    onPickedColor: (Color) -> Unit
) {
    var radius by remember {
        mutableStateOf(0f)
    }
    var pickerLocation by remember(radius) {
        mutableStateOf(Offset(radius, radius))
    }
    var pickerColor by remember {
        mutableStateOf(
            if (lightCenter) {
                Color.White
            } else {
                Color.Black
            }
        )
    }
    var brightness by remember {
        mutableStateOf(0f)
    }
    var alpha by remember {
        mutableStateOf(1f)
    }
    LaunchedEffect(brightness, pickerColor, alpha) {
        onPickedColor(
            Color(
                pickerColor.red().moveColorTo(!lightCenter, brightness),
                pickerColor.green().moveColorTo(!lightCenter, brightness),
                pickerColor.blue().moveColorTo(!lightCenter, brightness),
                (255 * alpha).roundToInt()
            )
        )
    }
    Column(modifier = Modifier.width(IntrinsicSize.Max)) {
        Canvas(modifier = modifier
            .size(200.dp)
            .onSizeChanged {
                radius = it.width / 2f
            }
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                        val angle =
                            (Math.toDegrees(
                                atan2(
                                    it.y - radius,
                                    it.x - radius
                                ).toDouble()
                            ) + 360) % 360
                        val length = getLength(it.x, it.y, radius)
                        val radiusProgress = 1 - (length / radius).coerceIn(0f, 1f)
                        val angleProgress = angle / 360f
                        val (rangeProgress, range) = ColorPickerHelper.calculateRangeProgress(
                            angleProgress
                        )
                        pickerColor = when (range) {
                            ColorRange.RedToYellow -> {
                                Color(
                                    red = 255.moveColorTo(lightCenter, radiusProgress),
                                    green = (255f * rangeProgress)
                                        .moveColorTo(lightCenter, radiusProgress)
                                        .roundToInt(),
                                    blue = 0.moveColorTo(lightCenter, radiusProgress),
                                )
                            }
                            ColorRange.YellowToGreen -> {
                                Color(
                                    red = (255 * (1 - rangeProgress))
                                        .moveColorTo(lightCenter, radiusProgress)
                                        .roundToInt(),
                                    green = 255.moveColorTo(lightCenter, radiusProgress),
                                    blue = 0.moveColorTo(lightCenter, radiusProgress),
                                )
                            }
                            ColorRange.GreenToCyan -> {
                                Color(
                                    red = 0.moveColorTo(lightCenter, radiusProgress),
                                    green = 255.moveColorTo(lightCenter, radiusProgress),
                                    blue = (255 * rangeProgress)
                                        .moveColorTo(lightCenter, radiusProgress)
                                        .roundToInt(),
                                )
                            }
                            ColorRange.CyanToBlue -> {
                                Color(
                                    red = 0.moveColorTo(lightCenter, radiusProgress),
                                    green = (255 * (1 - rangeProgress))
                                        .moveColorTo(lightCenter, radiusProgress)
                                        .roundToInt(),
                                    blue = 255.moveColorTo(lightCenter, radiusProgress),
                                )
                            }
                            ColorRange.BlueToPurple -> {
                                Color(
                                    red = (255 * rangeProgress)
                                        .moveColorTo(lightCenter, radiusProgress)
                                        .roundToInt(),
                                    green = 0.moveColorTo(lightCenter, radiusProgress),
                                    blue = 255.moveColorTo(lightCenter, radiusProgress),
                                )
                            }
                            ColorRange.PurpleToRed -> {
                                Color(
                                    red = 255.moveColorTo(lightCenter, radiusProgress),
                                    green = 0.moveColorTo(lightCenter, radiusProgress),
                                    blue = (255 * (1 - rangeProgress))
                                        .moveColorTo(lightCenter, radiusProgress)
                                        .roundToInt(),
                                )
                            }
                        }
                        pickerLocation = getBoundedPointWithInRadius(
                            it.x,
                            it.y,
                            length,
                            radius,
                            BoundedPointStrategy.Inside
                        )
                    }
                }
                return@pointerInteropFilter true
            }) {
            drawCircle(
                Brush.sweepGradient(gradientColors)
            )
            drawCircle(
                ShaderBrush(
                    RadialGradientShader(
                        Offset(size.width / 2f, size.height / 2f),
                        colors = listOf(
                            if (lightCenter) {
                                Color.White
                            } else {
                                Color.Black
                            }, Color.Transparent
                        ),
                        radius = size.width / 2f
                    )
                )
            )
            drawColorSelector(pickerColor, pickerLocation)
        }
        if (showBrightnessBar) {
            Spacer(modifier = Modifier.height(16.dp))
            ColorSlideBar(
                colors = listOf(
                    if (lightCenter) {
                        Color.Black
                    } else {
                        Color.White
                    }, pickerColor
                )
            ) {
                brightness = 1 - it
            }
        }
        if (showAlphaBar) {
            Spacer(modifier = Modifier.height(16.dp))
            ColorSlideBar(colors = listOf(Color.Transparent, pickerColor)) {
                alpha = it
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
private fun CircleColorPickerPreview() {
    CircleColorPicker(
        modifier = Modifier,
        showAlphaBar = true,
        showBrightnessBar = true,
        lightCenter = true,
        onPickedColor = {}
    )
}

private fun Int.moveColorTo(toWhite: Boolean, progress: Float): Int {
    return if (toWhite) {
        lighten(progress)
    } else {
        darken(progress)
    }
}

private fun Double.moveColorTo(toWhite: Boolean, progress: Float): Double {
    return if (toWhite) {
        lighten(progress)
    } else {
        darken(progress)
    }
}