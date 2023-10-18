package com.eduramza.pomodorotrack.ui.timer

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.eduramza.pomodorotrack.R
import com.eduramza.pomodorotrack.domain.entity.PomodoroCycle
import com.eduramza.pomodorotrack.ui.components.PomodoroControlButtons
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

            Text(
                text = activity.getString(getCycleTitle(viewModel.state.collectAsState().value.pomodoroCycle)),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Exibir o progresso do timer
            PomodoroTimerProgress(viewModel = viewModel)

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = activity.getString(
                    R.string.pomodoro_counter_info,
                    viewModel.state.collectAsState().value.pomodoroCounter.toString()
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Exibir os botões de controle
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
                            text = stringResource(R.string.exit_focus_mode),
                            modifier = Modifier.padding(8.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.want_exit_focus_mode_question),
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

private fun getCycleTitle(cycle: PomodoroCycle): Int {
    return when (cycle) {
        PomodoroCycle.Pomodoro -> R.string.pomodoro_cycle_title
        PomodoroCycle.ShortBreak -> R.string.short_break_cycle_title
        PomodoroCycle.LongBreak -> R.string.long_break_cycle_title
    }
}