package com.eduramza.pomodorotrack.domain.entity

enum class PomodoroCycle(val time: Long) {
    Pomodoro(25 * 60 * 1000L),
    ShortBreak(5 * 60 * 1000L),
    LongBreak(15 * 60 * 1000L)
}