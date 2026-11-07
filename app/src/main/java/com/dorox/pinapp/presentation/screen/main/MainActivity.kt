package com.dorox.pinapp.presentation.screen.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dorox.pinapp.BuildConfig
import com.dorox.pinapp.ui.theme.PinAppTheme
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            !viewModel.isAuth.value
        }

       Places.initializeWithNewPlacesApiEnabled(
           this,
           BuildConfig.MAPS_API_KEY
       )

        enableEdgeToEdge()
        setContent {
            PinAppTheme {
                MainScreen()
            }
        }
    }
}