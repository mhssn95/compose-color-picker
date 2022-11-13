package io.mhssn.colorpicker.ext

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.roundToInt

internal fun DrawScope.drawColorSelector(color: Color, location: Offset) {
    drawCircle(color, radius = 30f, center = location)
    drawCircle(Color.White, radius = 30f, center = location, style = Stroke(5f))
    drawCircle(Color.LightGray, radius = 30f, center = location, style = Stroke(1f))
}

internal fun DrawScope.drawTransparentBackground(verticalBoxesSize: Int = 10) {
    val boxSize = size.height / verticalBoxesSize
    repeat((size.width / boxSize).roundToInt() + 1) { x ->
        repeat(verticalBoxesSize) { y ->
            drawRect(
                if ((y + x) % 2 == 0) {
                    Color.LightGray
                } else {
                    Color.White
                }, topLeft = Offset(x * boxSize, y * boxSize), size = Size(boxSize, boxSize)
            )
        }
    }
}

fun Modifier.transparentBackground(verticalBoxesCount: Int = 10) = this.drawBehind {
    drawTransparentBackground(verticalBoxesCount)
}