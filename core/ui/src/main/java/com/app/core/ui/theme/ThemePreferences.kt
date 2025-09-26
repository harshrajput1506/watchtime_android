package com.app.core.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ThemePreferences(context: Context) {
    companion object {
        private const val PREF_NAME = "theme_preferences"
        private const val KEY_DARK_THEME = "dark_theme"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isDarkTheme(): Boolean {
        return prefs.getBoolean(KEY_DARK_THEME, true) // Default to dark theme
    }

    fun setDarkTheme(isDark: Boolean) {
        prefs.edit { putBoolean(KEY_DARK_THEME, isDark) }
    }
}
