package com.example.finalprojectpam.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.data.local.database.KancaDatabase
import com.example.finalprojectpam.data.local.entity.UserProfileEntity
import com.example.finalprojectpam.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(KancaDatabase.getInstance(application))

    val userProfile: StateFlow<UserProfileEntity?> = userRepository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun createOrUpdateProfile(name: String) {
        viewModelScope.launch {
            userRepository.createOrUpdateProfile(name)
        }
    }
}
