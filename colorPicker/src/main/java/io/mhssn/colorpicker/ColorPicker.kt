package io.mhssn.colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.mhssn.colorpicker.ext.toHex
import io.mhssn.colorpicker.ext.transparentBackground
import io.mhssn.colorpicker.pickers.CircleColorPicker
import io.mhssn.colorpicker.pickers.ClassicColorPicker
import io.mhssn.colorpicker.pickers.RingColorPicker
import io.mhssn.colorpicker.pickers.SimpleRingColorPicker

sealed class ColorPickerType {
    /**
     * @param showAlphaBar Sets the visibility of the alpha bar.
     */
    class Classic(val showAlphaBar: Boolean = true) : ColorPickerType()

    /**
     * @param showBrightnessBar Sets the visibility of the brightness bar.
     * @param showAlphaBar Sets the visibility of the alpha bar.
     * @param lightCenter Changes the center of the circle to black or white.
     */
    class Circle(
        val showBrightnessBar: Boolean = true,
        val showAlphaBar: Boolean = true,
        val lightCenter: Boolean = true
    ) : ColorPickerType()

    /**
     * @param ringWidth Sets the color ring width.
     * @param previewRadius Sets the radius of the center color preview circle.
     * @param showLightnessBar Sets the visibility of the lightness bar.
     * @param showDarknessBar Sets the visibility of the darkness bar.
     * @param showAlphaBar Sets the visibility of the alpha bar.
     * @param showColorPreview Sets the visibility of the center color preview circle.
     */
    class Ring(
        val ringWidth: Dp = 10.dp,
        val previewRadius: Dp = 80.dp,
        val showLightnessBar: Boolean = true,
        val showDarknessBar: Boolean = true,
        val showAlphaBar: Boolean = true,
        val initialColor: Color = Color.Red,
        val showColorPreview: Boolean = true
    ) : ColorPickerType()

    /**
     * @param colorWidth Arc width of all colors.
     * @param tracksCount Amount of the tracks.
     * @param sectorsCount Amount of the sectors for each track.
     */
    class SimpleRing(
        val colorWidth: Dp = 20.dp,
        val tracksCount: Int = 5,
        val sectorsCount: Int = 24,
    ) : ColorPickerType()
}

/**
 * @param type Color picker type example [ColorPickerType.Circle].
 * @param onPickedColor Executes when the user selects a color.
 */
@ExperimentalComposeUiApi
@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    type: ColorPickerType = ColorPickerType.Classic(),
    onPickedColor: (Color) -> Unit
) {
    Box(modifier = modifier) {
        when (type) {
            is ColorPickerType.Classic -> ClassicColorPicker(
                showAlphaBar = type.showAlphaBar,
                onPickedColor = onPickedColor,
            )
            is ColorPickerType.Circle -> CircleColorPicker(
                showAlphaBar = type.showAlphaBar,
                showBrightnessBar = type.showBrightnessBar,
                lightCenter = type.lightCenter,
                onPickedColor = onPickedColor
            )
            is ColorPickerType.Ring -> RingColorPicker(
                ringWidth = type.ringWidth,
                initialColor = type.initialColor,
                previewRadius = type.previewRadius,
                showLightColorBar = type.showLightnessBar,
                showDarkColorBar = type.showDarknessBar,
                showAlphaBar = type.showAlphaBar,
                showColorPreview = type.showColorPreview,
                onPickedColor = onPickedColor
            )
            is ColorPickerType.SimpleRing -> SimpleRingColorPicker(
                colorWidth = type.colorWidth,
                tracksCount = type.tracksCount,
                sectorsCount = type.sectorsCount,
                onPickedColor = onPickedColor
            )
        }
    }
}

/**
 * @param show Dialog Visibility.
 * @param onDismissRequest Executes when the user tries to dismiss the dialog.
 * @param properties [DialogProperties] for further customization of this dialog's behavior.
 * @param type Color picker type example [ColorPickerType.Classic].
 * @param onPickedColor Executes when the user selects a color from the color picker dialog.
 */
@ExperimentalComposeUiApi
@Composable
fun ColorPickerDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    type: ColorPickerType = ColorPickerType.Classic(),
    onPickedColor: (Color) -> Unit
) {
    var showDialog by remember(show) {
        mutableStateOf(show)
    }
    var color by remember {
        mutableStateOf(Color.White)
    }
    if (showDialog) {
        Dialog(onDismissRequest = {
            onDismissRequest()
            showDialog = false
        }, properties = properties) {
            Box(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White)
            ) {
                Box(modifier = Modifier.padding(32.dp)) {
                    Column {
                        ColorPicker(type = type, onPickedColor = {
                            color = it
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp, 30.dp)
                                    .clip(RoundedCornerShape(50))
                                    .border(0.3.dp, Color.LightGray, RoundedCornerShape(50))
                                    .transparentBackground(verticalBoxesAmount = 4)
                                    .background(color)
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color.Gray)) {
                                        append("#")
                                    }
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(color.toHex())
                                    }
                                },
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onPickedColor(color)
                                showDialog = false
                            },
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(text = "Select")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true, name = "Classic Color Picker")
@Composable
private fun ClassicColorPickerPreview() {
    ColorPicker(type = ColorPickerType.Classic()) {}
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true, name = "Circle Color Picker")
@Composable
private fun CircleColorPickerPreview() {
    ColorPicker(type = ColorPickerType.Circle()) {}
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true, name = "Ring Color Picker")
@Composable
private fun RingColorPickerPreview() {
    ColorPicker(type = ColorPickerType.Ring()) {}
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true, name = "Simple Color Picker")
@Composable
private fun SimpleColorPickerPreview() {
    ColorPicker(type = ColorPickerType.SimpleRing()) {}
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true, name = "Color Picker Dialog")
@Composable
private fun ColorPickerDialogPreview() {
    ColorPickerDialog(show = true, onDismissRequest = {}, onPickedColor = {})
}
