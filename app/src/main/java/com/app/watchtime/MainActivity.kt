package com.app.watchtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.app.core.navigation.AppNavigation
import com.app.core.ui.theme.ThemeViewModel
import com.app.core.ui.theme.WatchTimeTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel = getViewModel<ThemeViewModel>()
            WatchTimeTheme(
                viewModel = themeViewModel
            ) {
                AppNavigation(
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}