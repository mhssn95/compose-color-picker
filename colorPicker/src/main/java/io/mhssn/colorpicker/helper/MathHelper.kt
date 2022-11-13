package io.mhssn.colorpicker.helper

import androidx.compose.ui.geometry.Offset
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

internal enum class BoundedPointStrategy {
    Edge,
    Inside,
    Outside
}

internal object MathHelper {
    fun getLength(x: Float, y: Float, radius: Float): Float {
        return sqrt((x - radius) * (x - radius) + (y - radius) * (y - radius))
    }

    fun getBoundedPointWithInRadius(
        x: Float,
        y: Float,
        length: Float,
        radius: Float,
        strategy: BoundedPointStrategy
    ): Offset {
        return if (strategy == BoundedPointStrategy.Edge ||
            (strategy == BoundedPointStrategy.Inside && length > radius) ||
            (strategy == BoundedPointStrategy.Outside && length < radius)
        ) {
            val angle = atan2(y - radius, x - radius)
            Offset(x - (length - radius) * cos(angle), y - (length - radius) * sin(angle))
        } else {
            Offset(x, y)
        }
    }

}