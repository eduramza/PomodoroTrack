package com.eduramza.pomodorotrack.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.eduramza.pomodorotrack.R
import com.eduramza.pomodorotrack.ui.timer.CountdownTimerViewModel

@Composable
fun PomodoroControlButtons(
    viewModel: CountdownTimerViewModel,
    context: Context
) {
    val pomodoroState = viewModel.state.collectAsState().value
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (pomodoroState.timerRunning) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(gradient)
                        .padding(12.dp)
                        .clickable { viewModel.resetTimer() },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_replay),
                        contentDescription = "Previous Cycle",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(gradient)
                .padding(16.dp)
                .clickable { viewModel.controlCountdownTimer(context) }
        ) {
            Icon(
                painter = painterResource(id = pomodoroState.controlButton.icon),
                contentDescription = pomodoroState.controlButton.contentDescription,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        if (pomodoroState.timerRunning) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(gradient)
                        .padding(12.dp)
                        .clickable { viewModel.setNextCycle() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_skip_next),
                        contentDescription = "Skip Cycle",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}