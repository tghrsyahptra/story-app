package com.tghrsyahptra.storyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tghrsyahptra.storyapp.data.response.UserPreferencesManager
import com.tghrsyahptra.storyapp.ui.login.LoginViewModel
import com.tghrsyahptra.storyapp.ui.main.MainViewModel
import com.tghrsyahptra.storyapp.ui.register.RegisterViewModel

class ViewModelFactory(private val preferencesManager: UserPreferencesManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java -> LoginViewModel(preferencesManager) as T
            RegisterViewModel::class.java -> RegisterViewModel(preferencesManager) as T
            MainViewModel::class.java -> MainViewModel(preferencesManager) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}