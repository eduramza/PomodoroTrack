package com.eduramza.pomodorotrack.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eduramza.pomodorotrack.ui.timer.CountdownTimerViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun PomodoroTimerProgress(
    viewModel: CountdownTimerViewModel,
    fontSize: TextUnit = 64.sp,
    radius: Dp = 120.dp,
    strokeWidth: Dp = 16.dp
) {
    val state = viewModel.state.collectAsState().value

    val totalTime = state.pomodoroCycle.time.toFloat()
    val timeRemaining = state.timerRemaining.toFloat()
    val percentage = 1f - (timeRemaining / totalTime)

    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorSecondary = MaterialTheme.colorScheme.secondary

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f), onDraw = {
            val startAngleRadians = (-90f * (PI / 180f)).toFloat() // -90 graus em radianos
            val progressAngleRadians = (360f * percentage * (PI / 180f)).toFloat() // conversão do ângulo de progresso para radianos

            val gradientLength = 0.8f // 80% da extensão do progresso
            val startX = (size.width / 2 + (size.width / 2) * cos(startAngleRadians + progressAngleRadians * gradientLength))
            val startY = (size.height / 2 + (size.height / 2) * sin(startAngleRadians + progressAngleRadians * gradientLength))

            val endX = (size.width / 2)
            val endY = (size.height / 2)

            val gradientColor = Brush.linearGradient(
                colors = listOf(
                    colorPrimary,
                    colorSecondary
                ),
                start = Offset(startX, startY),
                end = Offset(endX, endY)
            )

            drawArc(
                brush = gradientColor,
                startAngle = -90f,
                sweepAngle = 360 * percentage,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        })
        Text(
            text = formatTime(timeRemaining.toLong()),
            fontSize = fontSize,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressPreview(){
    PomodoroTimerProgress(viewModel = CountdownTimerViewModel())
}


fun formatTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}