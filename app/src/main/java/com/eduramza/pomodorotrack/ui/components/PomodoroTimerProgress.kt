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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eduramza.pomodorotrack.domain.entity.PomodoroCycle
import com.eduramza.pomodorotrack.ui.timer.CountdownTimerViewModel


@Composable
fun PomodoroTimerProgress(
    viewModel: CountdownTimerViewModel,
    fontSize: TextUnit = 48.sp,
    radius: Dp = 100.dp,
    strokeWidth: Dp = 16.dp
) {
    val state = viewModel.state.collectAsState().value

    val totalTime = state.pomodoroCycle.time.toFloat()
    val timeRemaining = state.timerRemaining.toFloat()
    val percentage = 1f - (timeRemaining / totalTime)

    val color = MaterialTheme.colorScheme.primary

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f), onDraw = {
            drawArc(
                color = color,
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