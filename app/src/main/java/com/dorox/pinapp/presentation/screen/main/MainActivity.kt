package com.dorox.pinapp.presentation.screen.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.dorox.pinapp.data.repository.FireBaseAuthRepository
import com.dorox.pinapp.domain.repository.AuthRepository
import com.dorox.pinapp.ui.theme.PinAppTheme

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private lateinit var authRepository: AuthRepository
    private var isAuth = MutableStateFlow(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            PinAppTheme {
                MainScreen()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            !isAuth.value
        }

        lifecycleScope.launch {
            try {
                val uid = authRepository.provideAuthenticated()
                Log.d("Auth", "Авторизован с UID: $uid")
            } catch (e: Exception) {
                Log.e("Auth", "Ошибка авторизации", e)
            } finally {
                isAuth.value = true
            }
        }
    }
}