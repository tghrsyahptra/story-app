package com.tghrsyahptra.storyapp.ui.detailstory

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tghrsyahptra.storyapp.R
import com.tghrsyahptra.storyapp.databinding.ActivityDetailStoryBinding
import com.tghrsyahptra.storyapp.utils.toFormattedDate

class DetailStoryActivity : AppCompatActivity() {

    private var _binding: ActivityDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        populateStoryDetails()
    }

    private fun setupActionBar() {
        supportActionBar?.title = getString(R.string.detail_story)
    }

    private fun populateStoryDetails() {
        val photoUrl = intent.getStringExtra(PHOTO_URL)
        val name = intent.getStringExtra(NAME)
        val createAt = intent.getStringExtra(CREATE_AT)
        val description = intent.getStringExtra(DESCRIPTION)

        photoUrl?.let {
            loadPhoto(it)
        }

        binding.apply {
            tvDetailName.text = name
            tvDetailCreatedTime.text = createAt?.toFormattedDate()
            tvDetailDescription.text = description
        }
    }

    private fun loadPhoto(url: String) {
        Glide.with(this)
            .load(url)
            .into(binding.ivDetailPhoto)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val NAME = "name"
        const val CREATE_AT = "create_at"
        const val DESCRIPTION = "description"
        const val PHOTO_URL = "photoUrl"
    }
}