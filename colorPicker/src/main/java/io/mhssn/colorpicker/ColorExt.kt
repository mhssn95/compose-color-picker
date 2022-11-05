package io.mhssn.colorpicker

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.red(): Int {
    return toArgb() shr 16 and 0xff
}

fun Color.green(): Int {
    return toArgb() shr 8 and 0xff
}

fun Color.blue(): Int {
    return toArgb() and 0xff
}