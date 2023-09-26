package com.eduramza.pomodorotrack.ui.timer

import androidx.annotation.DrawableRes
import com.eduramza.pomodorotrack.R
import com.eduramza.pomodorotrack.domain.entity.PomodoroCycle

data class PomodoroUIState(
    val timerRemaining: Long = PomodoroCycle.Pomodoro.time,
    val pomodoroCycle: PomodoroCycle = PomodoroCycle.Pomodoro,
    val timerRunning: Boolean = false,
    val isLockedScreen: Boolean = false,
    val pomodoroCounter: Int = 0,
    val controlButton: ControlButton = ControlButton("Start", R.drawable.ic_play_arrow),
)

data class ControlButton(
    val contentDescription: String,
    @DrawableRes val icon: Int
)