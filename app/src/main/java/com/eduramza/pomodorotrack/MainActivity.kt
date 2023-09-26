package com.eduramza.pomodorotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eduramza.pomodorotrack.ui.theme.PomodoroAppTheme
import com.eduramza.pomodorotrack.ui.timer.CountdownTimerViewModel
import com.eduramza.pomodorotrack.ui.timer.PomodoroScreen

class MainActivity : ComponentActivity() {
    private val viewModel: CountdownTimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            PomodoroAppTheme(pomodoroCycle = viewModel.state.collectAsState().value.pomodoroCycle) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    PomodoroScreen(viewModel = viewModel, this@MainActivity)
                }
            }
        }
    }
}