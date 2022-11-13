package io.mhssn.colorpicker.helper

import io.mhssn.colorpicker.data.ColorRange


internal object ColorPickerHelper {
    fun calculateRangeProgress(progress: Double): Pair<Double, ColorRange> {
        val range: ColorRange
        return progress * 6 - when {
            progress < 1f / 6 -> {
                range = ColorRange.RedToYellow
                0
            }
            progress < 2f / 6 -> {
                range = ColorRange.YellowToGreen
                1
            }
            progress < 3f / 6 -> {
                range = ColorRange.GreenToCyan
                2
            }
            progress < 4f / 6 -> {
                range = ColorRange.CyanToBlue
                3
            }
            progress < 5f / 6 -> {
                range = ColorRange.BlueToPurple
                4
            }
            else -> {
                range = ColorRange.PurpleToRed
                5
            }
        } to range
    }
}