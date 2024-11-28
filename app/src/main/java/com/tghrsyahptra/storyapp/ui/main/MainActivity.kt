package com.tghrsyahptra.storyapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tghrsyahptra.storyapp.R
import com.tghrsyahptra.storyapp.data.response.ListStoryItem
import com.tghrsyahptra.storyapp.data.response.UserPreferencesManager
import com.tghrsyahptra.storyapp.databinding.ActivityMainBinding
import com.tghrsyahptra.storyapp.ui.ViewModelFactory
import com.tghrsyahptra.storyapp.ui.addstory.AddStoryActivity
import com.tghrsyahptra.storyapp.ui.login.LoginActivity
import com.tghrsyahptra.storyapp.ui.login.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()
        initializeViewModels()
        observeViewModelData()
        setupRecyclerView()
        setupAddStoryButton()
    }

    private fun setupActionBar() {
        supportActionBar?.title = getString(R.string.dashboard)
    }

    private fun initializeViewModels() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferencesManager.getInstance(dataStore))
        )[MainViewModel::class.java]

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferencesManager.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun observeViewModelData() {
        mainViewModel.stories.observe(this) { storyList ->
            val adapter = MainAdapter(storyList as ArrayList<ListStoryItem>)
            binding?.rvListStory?.adapter = adapter
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        loginViewModel.getUser().observe(this) { user ->
            if (user.userId.isEmpty()) {
                navigateToLogin()
            } else {
                mainViewModel.fetchStories(user.token)
            }
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        binding?.rvListStory?.apply {
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }
    }

    private fun setupAddStoryButton() {
        binding?.btnAddStory?.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                loginViewModel.signOut()
                true
            }
            R.id.settings_language -> {
                openLanguageSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openLanguageSettings() {
        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}