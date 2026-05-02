package com.example.finalprojectpam.data.repository

import com.example.finalprojectpam.data.local.preferences.AuthPreferences

class AuthRepository(private val prefs: AuthPreferences) {

    fun register(name: String, email: String, password: String, role: String) =
        prefs.register(name, email, password, role)

    fun login(email: String, password: String): Boolean =
        prefs.login(email, password)

    fun logout() = prefs.logout()

    fun isLoggedIn(): Boolean = prefs.isLoggedIn()

    fun isRegistered(): Boolean = prefs.isRegistered()

    fun getName(): String? = prefs.getName()

    fun getRole(): String? = prefs.getRole()
}
