package com.eduramza.pomodorotrack.ui.timer

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduramza.pomodorotrack.R
import com.eduramza.pomodorotrack.domain.entity.PomodoroCycle
import com.eduramza.pomodorotrack.services.NotificationHelper
import com.eduramza.pomodorotrack.ui.AppLifecycleObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CountdownTimerViewModel() : ViewModel(), LifecycleObserver {

    companion object {
        const val ONE_SECOND = 1000L

        private var INSTANCE: CountdownTimerViewModel? = null

        fun getInstance(): CountdownTimerViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CountdownTimerViewModel().also { INSTANCE = it }
            }
        }
    }

    private val _state = MutableStateFlow(PomodoroUIState())
    val state: StateFlow<PomodoroUIState> = _state.asStateFlow()

    private var timer: CountDownTimer? = null

    private var unlockedManually = false
    val showSnackbar = mutableStateOf(false)

    val appLifecycleObserver = AppLifecycleObserver()

    fun controlCountdownTimer(context: Context) {
        if (_state.value.timerRunning) {
            pauseTimer()
        } else {
            startTimer(context)
        }
    }

    fun unlockScreen() {
        unlockedManually = true
        _state.value = _state.value.copy(
            isLockedScreen = false
        )
    }

    private fun startTimer(context: Context) {
        if (_state.value.timerRunning) return

        timer = object : CountDownTimer(_state.value.timerRemaining, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _state.value = _state.value.copy(
                    timerRemaining = millisUntilFinished,
                    timerRunning = true,
                    isLockedScreen = getIsScreenLocked(),
                    controlButton = ControlButton("Pause", R.drawable.ic_pause)
                )
                verifyIfInBackground(context)
            }

            override fun onFinish() {
                setNextCycle()
                playAlarmSound(context)
            }
        }

        timer?.start()
    }

    private fun getIsScreenLocked() =
        _state.value.pomodoroCycle == PomodoroCycle.Pomodoro && !unlockedManually

    private fun playAlarmSound(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.pomodoro_alarm)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
    }

    fun setNextCycle() {
        pauseTimer()

        when (_state.value.pomodoroCycle) {
            PomodoroCycle.Pomodoro -> {
                val newCounter = _state.value.pomodoroCounter + 1
                if (newCounter % 4 == 0) {
                    _state.value = _state.value.copy(
                        pomodoroCycle = PomodoroCycle.LongBreak,
                        timerRemaining = PomodoroCycle.LongBreak.time,
                        pomodoroCounter = newCounter
                    )
                } else {
                    _state.value = _state.value.copy(
                        pomodoroCycle = PomodoroCycle.ShortBreak,
                        timerRemaining = PomodoroCycle.ShortBreak.time,
                        pomodoroCounter = newCounter
                    )
                }
            }

            else -> {
                unlockedManually = false
                _state.value = _state.value.copy(
                    pomodoroCycle = PomodoroCycle.Pomodoro,
                    timerRemaining = PomodoroCycle.Pomodoro.time
                )
            }
        }
    }

    private fun pauseTimer() {
        timer?.cancel()
        _state.value = _state.value.copy(
            timerRunning = false,
            isLockedScreen = false,
            controlButton = ControlButton("Start", R.drawable.ic_play_arrow)
        )
    }

    fun resetTimer() {
        timer?.cancel()
        _state.value = _state.value.copy(
            timerRemaining = _state.value.pomodoroCycle.time,
            timerRunning = false,
            pomodoroCounter = 0,
            controlButton = ControlButton("Start", R.drawable.ic_play_arrow)
        )
    }

    //region -lock unlock Snackbar
    val snackbarTimeout = 5000L  // 1000L = 1s
    fun handleScreenTap() {
        if (_state.value.isLockedScreen) {
            showSnackbar.value = true
        }
    }

    fun dismissSnackbar() {
        showSnackbar.value = false
    }

    fun exitFocusMode() {
        unlockScreen()
        dismissSnackbar()
    }

    fun startSnackbarTimer() {
        viewModelScope.launch {
            delay(snackbarTimeout)
            dismissSnackbar()
        }
    }
    //endregion

    //region - notification
    private fun verifyIfInBackground(context: Context) {
        if (appLifecycleObserver.isAppInBackground.value == true) {
            updateNotification(context)
        } else {
            NotificationHelper.hideNotification(context)
        }
    }

    private fun updateNotification(context: Context) {
        val notification = NotificationHelper.buildNotification(context, _state.value)
        notification?.let { NotificationHelper.showNotification(context, it) }
    }

    fun onResume(context: Context) {
        if (_state.value.timerRunning) {
            startTimer(context)
        } else {
            pauseTimer()
        }
    }
    //endregion

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}
