package org.example.app.ui

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

/**
 * PUBLIC_INTERFACE
 * PreferencesHelper
 *
 * Centralized access for UI-related preferences such as theme mode and display name.
 */
object PreferencesHelper {

    private const val PREFS_NAME = "ui_prefs"
    private const val KEY_DARK_MODE = "dark_mode"
    private const val KEY_DISPLAY_NAME = "display_name"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // PUBLIC_INTERFACE
    fun isDarkMode(context: Context): Boolean =
        prefs(context).getBoolean(KEY_DARK_MODE, false)

    // PUBLIC_INTERFACE
    fun setDarkMode(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_DARK_MODE, enabled).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    // PUBLIC_INTERFACE
    fun getDisplayName(context: Context): String =
        prefs(context).getString(KEY_DISPLAY_NAME, "Gokul") ?: "Gokul"

    // PUBLIC_INTERFACE
    fun setDisplayName(context: Context, name: String) {
        prefs(context).edit().putString(KEY_DISPLAY_NAME, name).apply()
    }
}
