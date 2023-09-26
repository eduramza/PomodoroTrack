package com.eduramza.pomodorotrack.ui.components

import android.content.Context
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

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (pomodoroState.timerRunning) {
            Button(
                onClick = viewModel::resetTimer,
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Box(modifier = Modifier.defaultMinSize(32.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_replay),
                        contentDescription = "Previous Cycle",
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.size(80.dp))
        }
        Button(
            onClick = {
                viewModel.controlCountdownTimer(context)
            },
            shape = CircleShape,
            modifier = Modifier.size(100.dp)
        ) {
            Icon(
                painter = painterResource(id = pomodoroState.controlButton.icon),
                contentDescription = pomodoroState.controlButton.contentDescription,
                modifier = Modifier.size(32.dp)
            )
        }

        if (pomodoroState.timerRunning) {
            Button(
                onClick = viewModel::setNextCycle,
                shape = CircleShape,
                modifier = Modifier.size(80.dp)
            ) {
                Box(modifier = Modifier.defaultMinSize(32.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_skip_next),
                        contentDescription = "Skip Cycle",
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.size(80.dp))
        }
    }
}