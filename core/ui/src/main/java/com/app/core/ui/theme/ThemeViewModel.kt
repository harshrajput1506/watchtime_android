package com.app.core.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel(
    private val themePreferences: ThemePreferences
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(themePreferences.isDarkTheme())
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        val newTheme = !_isDarkTheme.value
        _isDarkTheme.value = newTheme
        themePreferences.setDarkTheme(newTheme)
    }
}
