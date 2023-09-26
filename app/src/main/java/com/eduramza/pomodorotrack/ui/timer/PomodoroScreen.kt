package com.eduramza.pomodorotrack.ui.timer

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eduramza.pomodorotrack.R
import com.eduramza.pomodorotrack.ui.components.PomodoroTimerProgress

@Composable
fun PomodoroScreen(
    viewModel: CountdownTimerViewModel,
    activity: Activity
) {
    val isLocked = viewModel.state.collectAsState().value.isLockedScreen
    val showSnackbar = viewModel.showSnackbar.value

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Exibir o progresso do timer
            PomodoroTimerProgress(viewModel = viewModel)

            // Espaçamento entre o progresso e a contagem de pomodoros
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Pomodoros Completos #${viewModel.state.collectAsState().value.pomodoroCounter}")

            // Espaçamento entre a contagem de pomodoros e os botões
            Spacer(modifier = Modifier.height(16.dp))

            // Botões de controle
            PomodoroControlButtons(viewModel, activity.applicationContext)

        }
        if (isLocked) {
            // Bloqueador transparente
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            viewModel.handleScreenTap()
                        }
                    }
            )
        }

        if (showSnackbar) {
            LaunchedEffect(key1 = showSnackbar) {
                viewModel.startSnackbarTimer()
            }

            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { viewModel.exitFocusMode() }) {
                        Text(
                            text = "Sair",
                            modifier = Modifier.padding(8.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            ) {
                Text(
                    text = "Deseja realmente sair do modo foco?",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }

    LaunchedEffect(isLocked) {
        if (isLocked) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@Composable
private fun UnlockScreenContent(
    viewModel: CountdownTimerViewModel,
    isLocked: MutableState<Boolean>
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (isLocked.value) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lock_clock),
                contentDescription = "Locked",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                viewModel.unlockScreen()
                            }
                        )
                    }
            )
        }
    }
}


@Composable
private fun PomodoroControlButtons(
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