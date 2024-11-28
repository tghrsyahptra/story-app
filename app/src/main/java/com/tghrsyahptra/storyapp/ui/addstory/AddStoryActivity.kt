package com.tghrsyahptra.storyapp.ui.addstory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.tghrsyahptra.storyapp.R
import com.tghrsyahptra.storyapp.data.response.UserPreferencesManager
import com.tghrsyahptra.storyapp.databinding.ActivityAddStoryBinding
import com.tghrsyahptra.storyapp.ui.ViewModelFactory
import com.tghrsyahptra.storyapp.ui.login.LoginViewModel
import com.tghrsyahptra.storyapp.ui.main.MainActivity
import com.tghrsyahptra.storyapp.ui.main.MainViewModel
import com.tghrsyahptra.storyapp.utils.createTempFileForCamera
import com.tghrsyahptra.storyapp.utils.compressImageFile
import com.tghrsyahptra.storyapp.utils.convertUriToFile
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

class AddStoryActivity : AppCompatActivity() {
    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var currentPhotoPath: String
    private var selectedFile: File? = null

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = getString(R.string.add_story)

        initViewModels()
        checkPermissions()
        setupClickListeners()
    }

    private fun initViewModels() {
        val preferencesManager = UserPreferencesManager.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, ViewModelFactory(preferencesManager))[MainViewModel::class.java]
        loginViewModel = ViewModelProvider(this, ViewModelFactory(preferencesManager))[LoginViewModel::class.java]
    }

    private fun checkPermissions() {
        if (!arePermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun arePermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS && !arePermissionsGranted()) {
            showPermissionDeniedMessage()
            finish()
        }
    }

    private fun showPermissionDeniedMessage() {
        Toast.makeText(this, getString(R.string.permission), Toast.LENGTH_SHORT).show()
    }

    private fun setupClickListeners() {
        binding?.apply {
            btnAddCamera.setOnClickListener { openCamera() }
            btnAddGalery.setOnClickListener { openGallery() }
            buttonAdd.setOnClickListener { uploadStory() }
        }
    }

    private fun uploadStory() {
        val description = binding?.edAddDescription?.text.toString()

        if (isInputValid(description)) {
            val compressedFile = compressImageFile(selectedFile ?: return)
            loginViewModel.getUser().observe(this) { user ->
                mainViewModel.uploadStory(user.token, compressedFile, description)
                observeLoadingState()
            }
        } else {
            showInputError(description)
        }
    }

    private fun isInputValid(description: String) = selectedFile != null && description.isNotEmpty()

    private fun showInputError(description: String) {
        val message = if (description.isEmpty()) {
            getString(R.string.description_empty)
        } else {
            getString(R.string.image_empty)
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun observeLoadingState() {
        mainViewModel.isLoading.observe(this) { isLoading ->
            updateLoadingState(isLoading)
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE

        if (!isLoading) {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        finish()
    }

    private fun openGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        galleryResultLauncher.launch(chooser)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            resolveActivity(packageManager)
        }

        createTempFileForCamera(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.tghrsyahptra.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            cameraResultLauncher.launch(intent)
        }
    }

    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            handleCameraResult()
        }
    }

    private fun handleCameraResult() {
        val photoFile = File(currentPhotoPath)
        selectedFile = photoFile
        binding?.tvAddImg?.setImageBitmap(BitmapFactory.decodeFile(photoFile.path))
    }

    private val galleryResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            handleGalleryResult(result)
        }
    }

    private fun handleGalleryResult(result: ActivityResult) {
        val selectedImageUri: Uri = result.data?.data ?: return
        selectedFile = convertUriToFile(selectedImageUri, this)
        binding?.tvAddImg?.setImageURI(selectedImageUri)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}