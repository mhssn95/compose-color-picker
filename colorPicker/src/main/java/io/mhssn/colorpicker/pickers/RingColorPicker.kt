package io.mhssn.colorpicker.pickers

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.mhssn.colorpicker.data.ColorRange
import io.mhssn.colorpicker.data.Colors.gradientColors
import io.mhssn.colorpicker.ext.*
import io.mhssn.colorpicker.helper.BoundedPointStrategy
import io.mhssn.colorpicker.helper.ColorPickerHelper
import io.mhssn.colorpicker.helper.MathHelper
import io.mhssn.colorpicker.helper.MathHelper.getBoundedPointWithInRadius
import io.mhssn.colorpicker.helper.MathHelper.getLength
import kotlin.math.atan2
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
@Composable
internal fun RingColorPicker(
    modifier: Modifier = Modifier,
    ringWidth: Dp,
    previewRadius: Dp,
    showLightColorBar: Boolean,
    showDarkColorBar: Boolean,
    showAlphaBar: Boolean,
    showColorPreview: Boolean,
    onPickedColor: (Color) -> Unit,
) {
    val density = LocalDensity.current
    val ringWidthPx = remember {
        with(density) { ringWidth.toPx() }
    }
    val previewRadiusPx = remember {
        with(density) { previewRadius.toPx() }
    }
    var radius by remember {
        mutableStateOf(0f)
    }
    var pickerLocation by remember(radius) {
        mutableStateOf(
            getBoundedPointWithInRadius(
                radius * 2,
                radius,
                getLength(radius * 2, radius, radius),
                radius - ringWidthPx / 2,
                BoundedPointStrategy.Edge
            )
        )
    }
    var selectedColor by remember {
        mutableStateOf(Color.Red)
    }
    var color by remember {
        mutableStateOf(Color.Red)
    }
    var lightColor by remember {
        mutableStateOf(Color.Red)
    }
    var darkColor by remember {
        mutableStateOf(Color.Red)
    }
    var lightness by remember {
        mutableStateOf(0f)
    }
    var darkness by remember {
        mutableStateOf(0f)
    }
    var alpha by remember {
        mutableStateOf(1f)
    }
    LaunchedEffect(selectedColor, lightness, darkness, alpha) {
        var red = selectedColor.red().lighten(lightness)
        var green = selectedColor.green().lighten(lightness)
        var blue = selectedColor.blue().lighten(lightness)
        lightColor = Color(red, green, blue, 255)
        red = red.darken(darkness)
        green = green.darken(darkness)
        blue = blue.darken(darkness)
        darkColor = Color(red, green, blue, 255)
        color = Color(red, green, blue, (255 * alpha).roundToInt())
        onPickedColor(color)
    }
    Column(modifier = Modifier.width(IntrinsicSize.Max)) {
        Canvas(modifier = modifier
            .size(200.dp)
            .onSizeChanged {
                radius = it.width.toFloat() / 2
            }
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                        val red: Int
                        val green: Int
                        val blue: Int
                        val angle =
                            (Math.toDegrees(
                                atan2(
                                    it.y - radius,
                                    it.x - radius
                                ).toDouble()
                            ) + 360) % 360
                        val length = MathHelper.getLength(it.x, it.y, radius)
                        val progress = angle / 360f
                        val (rangeProgress, range) = ColorPickerHelper.calculateRangeProgress(
                            progress
                        )
                        when (range) {
                            ColorRange.RedToYellow -> {
                                red = 255
                                green = (255f * rangeProgress).roundToInt()
                                blue = 0
                            }
                            ColorRange.YellowToGreen -> {
                                red = (255 * (1 - rangeProgress)).roundToInt()
                                green = 255
                                blue = 0
                            }
                            ColorRange.GreenToCyan -> {
                                red = 0
                                green = 255
                                blue = (255 * rangeProgress).roundToInt()
                            }
                            ColorRange.CyanToBlue -> {
                                red = 0
                                green = (255 * (1 - rangeProgress)).roundToInt()
                                blue = 255
                            }
                            ColorRange.BlueToPurple -> {
                                red = (255 * rangeProgress).roundToInt()
                                green = 0
                                blue = 255
                            }
                            ColorRange.PurpleToRed -> {
                                red = 255
                                green = 0
                                blue = (255 * (1 - rangeProgress)).roundToInt()
                            }
                        }
                        pickerLocation = getBoundedPointWithInRadius(
                            it.x,
                            it.y,
                            length,
                            radius - ringWidthPx / 2,
                            BoundedPointStrategy.Edge
                        )
                        selectedColor = Color(red, green, blue)
                    }
                }
                return@pointerInteropFilter true
            }) {
            drawCircle(
                Brush.sweepGradient(gradientColors),
                radius = radius - ringWidthPx / 2f,
                style = Stroke(ringWidthPx)
            )
            if (showColorPreview) {
                drawCircle(
                    color,
                    radius = previewRadiusPx
                )
            }
            drawColorSelector(selectedColor, pickerLocation)
        }
        if (showLightColorBar) {
            Spacer(modifier = Modifier.height(16.dp))
            ColorSlideBar(
                colors = listOf(
                    Color.White,
                    selectedColor
                )
            ) {
                lightness = 1 - it
            }
        }
        if (showDarkColorBar) {
            Spacer(modifier = Modifier.height(16.dp))
            ColorSlideBar(colors = listOf(Color.Black, lightColor)) {
                darkness = 1 - it
            }
        }
        if (showAlphaBar) {
            Spacer(modifier = Modifier.height(16.dp))
            ColorSlideBar(colors = listOf(Color.Transparent, darkColor)) {
                alpha = it
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
private fun RingColorPickerPreview() {
    RingColorPicker(
        modifier = Modifier,
        ringWidth = 20.dp,
        previewRadius = 70.dp,
        showLightColorBar = true,
        showDarkColorBar = true,
        showAlphaBar = true,
        showColorPreview = true,
        onPickedColor = {}
    )
}