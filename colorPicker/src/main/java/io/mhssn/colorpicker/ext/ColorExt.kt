package io.mhssn.colorpicker.ext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.math.roundToInt

fun Color.argb(): Array<Int> {
    val argb = toArgb()
    val alpha = argb shr 24 and 0xff
    val red = argb shr 16 and 0xff
    val green = argb shr 8 and 0xff
    val blue = argb and 0xff
    return arrayOf(alpha, red, green, blue)
}

fun Color.red(): Int {
    return toArgb() shr 16 and 0xff
}

fun Color.green(): Int {
    return toArgb() shr 8 and 0xff
}

fun Color.blue(): Int {
    return toArgb() and 0xff
}

fun Color.alpha(): Int {
    return toArgb() shr 24 and 0xff
}

fun Color.toHex(): String {
    val (alpha, red, green, blue) = argb()
    return alpha.toHex().plus(red.toHex()).plus(green.toHex()).plus(blue.toHex())
}

private fun Int.toHex(): String {
    return Integer.toHexString(this).let {
        if (it.length == 1) {
            "0$it"
        }
        else {
            it
        }
    }
}

internal fun Double.lighten(lightness: Float): Double {
    return this + (255 - this) * lightness
}

internal fun Float.lighten(lightness: Float): Float {
    return this + (255 - this) * lightness
}

internal fun Int.lighten(lightness: Float): Int {
    return (this + (255 - this) * lightness).roundToInt()
}

internal fun Double.darken(darkness: Float): Double {
    return this - this * darkness
}

internal fun Float.darken(darkness: Float): Float {
    return this - this * darkness
}

internal fun Int.darken(darkness: Float): Int {
    return (this - this * darkness).roundToInt()
}