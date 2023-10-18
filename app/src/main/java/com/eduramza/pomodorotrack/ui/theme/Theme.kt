package com.eduramza.pomodorotrack.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.eduramza.pomodorotrack.domain.entity.PomodoroCycle

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.White,
    secondary = DarkSecondary,
    background = DarkMidnightBackground,
    onBackground = Color.White,
    onSurface = Color.White,
)

//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)

private val PomodoroColorScheme = lightColorScheme(
    primary = WorkCyclePrimary,
    onPrimary = Color.Black,
    secondary = WorkCycleSecondary,
    onBackground = Color.Black,
)

private val ShortBreakColorScheme = lightColorScheme(
    primary = ShortBreakPrimary,
    onPrimary = Color.Black,
    secondary = ShortBreakSecondary,
    onBackground = Color.Black,
)

private val LongBreakColorScheme = lightColorScheme(
    primary = LongBreakPrimary,
    onPrimary = Color.Black,
    secondary = LongBreakSecondary,
    onBackground = Color.Black,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> PomodoroColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun PomodoroAppTheme(
    pomodoroCycle: PomodoroCycle,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
){
    val colorScheme = if (darkTheme){
        DarkColorScheme
    } else {
        when(pomodoroCycle){
            PomodoroCycle.Pomodoro -> PomodoroColorScheme
            PomodoroCycle.ShortBreak -> ShortBreakColorScheme
            PomodoroCycle.LongBreak -> LongBreakColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}