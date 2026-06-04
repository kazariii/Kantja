package com.example.finalprojectpam.data.local.session

import android.content.Context
import android.content.SharedPreferences

object SimpleSession {
    private const val PREF_NAME = "simple_auth_session"
    private const val KEY_USER_ID = "user_id"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUser(userId: String) {
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun currentUserId(): String? = prefs.getString(KEY_USER_ID, null)

    fun isLoggedIn(): Boolean = currentUserId() != null
}
