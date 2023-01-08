package io.rezyfr.trackerr.core.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    progressMax: Float = 100f,
    progressBarWidth: Dp = 12.dp,
    progressPercentage: Float = 1.0f,
    chartData: List<CircularChartData>
) {
    val painter = rememberVectorPainter(image = Icons.Default.AccountCircle)
    val asyncPainter = rememberAsyncImagePainter(model = "")
    Canvas(
        Modifier
            .size(150.dp)
            .padding(10.dp)
    ) {
        chartData.forEachIndexed { index, it ->
            drawArc(
                color = it.color,
                it.startAngle,
                it.progress,
                false,
                style = Stroke(10.dp.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
//            val angleInDegrees = (it.progress / 2.0) + (it.startAngle - 90)
//            val radius = (size.height / 2)
//            val x = -(radius * sin(Math.toRadians(angleInDegrees))).toFloat() + (size.width / 2)
//            val y = (radius * cos(Math.toRadians(angleInDegrees))).toFloat() + (size.height / 2)

            val offset = it.getOffset(size)
            with(painter) {
                translate(left = offset.x - 4.dp.toPx(), top = offset.y - 4.dp.toPx()){
                    draw(Size(20f, 20f), colorFilter = ColorFilter.tint(Color.Green))
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
fun PreviewChart() {
    CircularProgressBar(
        chartData = listOf<CircularChartData>(
            CircularChartData(
                color = Color.Red,
                progress = 25f / 100f * 360f,
                startAngle = 270f
            ),
            CircularChartData(
                color = Color.Blue,
                progress = 25f / 100f * 360f,
                startAngle = 270f + (25f / 100f) * 360f
            ),
            CircularChartData(
                color = Color.Yellow,
                progress = 25f / 100f * 360f,
                startAngle = 270f + (50f / 100f) * 360f
            ),
            CircularChartData(
                color = Color.Magenta,
                progress = 25f / 100f * 360f,
                startAngle = 270f + (75f / 100f) * 360f
            )
        )
    )
}

data class CircularChartData(
    val color: Color,
    val progress: Float,
    val startAngle: Float,
) {
    fun getOffset(size: Size): Offset {
        val angleInDegrees = (progress / 2.0) + (startAngle - 90)
        val radius = (size.height / 2)
        val x = -(radius * sin(Math.toRadians(angleInDegrees))).toFloat() + (size.width / 2)
        val y = (radius * cos(Math.toRadians(angleInDegrees))).toFloat() + (size.height / 2)
        return Offset(x, y)
    }
}