package com.dorox.pinapp.presentation.screen.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.dorox.pinapp.data.repository.AuthRepository
import com.dorox.pinapp.presentation.navigation.NavGraph
import com.dorox.pinapp.ui.theme.Orange100
import com.dorox.pinapp.ui.theme.PinAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authRepository = AuthRepository()
    private var isAuth = false

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            !isAuth
        }

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Orange100.toArgb(),
                darkScrim = Orange100.toArgb()
            )
        )

        setContent {
            PinAppTheme {
                MainScreen()
            }
        }

        lifecycleScope.launch {
            try {
                makeAuthentification()
            } catch (e: Exception) {
                Log.e("Auth", "Ошибка авторизации", e)
            } finally {
                isAuth = true
            }
        }
    }

    private suspend fun makeAuthentification() {
        val uid = authRepository.getCurrentUserUid()
        Log.d("Splash", "Текущий UID: $uid")

        if (uid == null) {
            val result = authRepository.signInAnonymously()
            if (result.isSuccess) {
                val newUid = result.getOrNull()
                Log.d("Auth", "Вход гостя $newUid выполнент успешно")
            } else {
                val error = result.exceptionOrNull()?.message ?: "Ошибка"
                Log.e("Auth", "Ошибка входа: $error")
            }
        }
    }
}