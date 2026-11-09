package com.example.gymapp.data.local

import android.content.Context

class SessionManager(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun saveToken(token: String) {
        preferences.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? = preferences.getString(KEY_TOKEN, null)

    fun clearToken() {
        preferences.edit().remove(KEY_TOKEN).apply()
    }

    companion object {
        private const val PREFERENCES_NAME = "gym_session"
        private const val KEY_TOKEN = "auth_token"
    }
}
