package com.tghrsyahptra.storyapp.ui.splashscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.tghrsyahptra.storyapp.ui.ViewModelFactory
import com.tghrsyahptra.storyapp.data.response.UserPreferencesManager
import com.tghrsyahptra.storyapp.databinding.ActivitySplashscreenBinding
import com.tghrsyahptra.storyapp.ui.login.LoginActivity
import com.tghrsyahptra.storyapp.ui.login.LoginViewModel
import com.tghrsyahptra.storyapp.ui.main.MainActivity
import com.tghrsyahptra.storyapp.ui.main.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

class Splashscreen : AppCompatActivity() {

    private var _binding: ActivitySplashscreenBinding? = null
    private val binding get() = _binding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        initializeViewModels()

        observeUserAuthentication()
    }

    private fun initializeViewModels() {
        val userPreferencesManager = UserPreferencesManager.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, ViewModelFactory(userPreferencesManager))[MainViewModel::class.java]
        loginViewModel = ViewModelProvider(this, ViewModelFactory(userPreferencesManager))[LoginViewModel::class.java]
    }

    private fun observeUserAuthentication() {
        loginViewModel.getUser().observe(this) { user ->
            val nextActivity = if (user.userId.isEmpty()) {
                LoginActivity::class.java
            } else {
                MainActivity::class.java
            }

            navigateToNextActivity(nextActivity)
        }
    }

    private fun navigateToNextActivity(activityClass: Class<*>) {
        Handler().postDelayed({
            val intent = Intent(this, activityClass)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val SPLASH_TIME_OUT: Long = 2000L
    }
}