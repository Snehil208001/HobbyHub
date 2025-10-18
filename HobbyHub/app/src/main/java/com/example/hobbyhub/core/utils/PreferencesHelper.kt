package com.example.hobbyhub.core.utils
import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {

    private const val PREFS_NAME = "HobbyHubPrefs"
    private const val KEY_REMEMBER_ME = "rememberMe"
    private const val KEY_SAVED_EMAIL = "savedEmail"
    private const val KEY_SAVED_PASSWORD = "savedPassword" // !! Insecure !!

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setRememberMe(context: Context, remember: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_REMEMBER_ME, remember).apply()
    }

    fun shouldRememberMe(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_REMEMBER_ME, false)
    }

    fun saveCredentials(context: Context, email: String, password: String) {
        getPreferences(context).edit()
            .putString(KEY_SAVED_EMAIL, email)
            .putString(KEY_SAVED_PASSWORD, password) // !! Insecure !!
            .apply()
    }

    fun getSavedEmail(context: Context): String? {
        return getPreferences(context).getString(KEY_SAVED_EMAIL, null)
    }

    fun getSavedPassword(context: Context): String? {
        // Only return password if remember me was actually checked
        if (shouldRememberMe(context)) {
            return getPreferences(context).getString(KEY_SAVED_PASSWORD, null) // !! Insecure !!
        }
        return null
    }

    fun clearCredentials(context: Context) {
        getPreferences(context).edit()
            .remove(KEY_SAVED_EMAIL)
            .remove(KEY_SAVED_PASSWORD) // !! Insecure !!
            .apply()
    }
}