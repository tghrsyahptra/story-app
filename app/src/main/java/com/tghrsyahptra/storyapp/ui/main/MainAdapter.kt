package com.tghrsyahptra.storyapp.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tghrsyahptra.storyapp.data.response.ListStoryItem
import com.tghrsyahptra.storyapp.databinding.ItemStoryBinding
import com.tghrsyahptra.storyapp.ui.detailstory.DetailStoryActivity
import com.tghrsyahptra.storyapp.utils.toFormattedDate

class MainAdapter(private val stories: List<ListStoryItem>) : RecyclerView.Adapter<MainAdapter.StoryViewHolder>() {

    class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            loadImage(story.photoUrl)
            displayStoryDetails(story)
            setupClickListener(story)
        }

        private fun loadImage(photoUrl: String?) {
            Glide.with(binding.root.context)
                .load(photoUrl)
                .into(binding.imgItemPhoto)
        }

        private fun displayStoryDetails(story: ListStoryItem) {
            binding.tvItemName.text = story.name
            binding.tvItemCreated.text = story.createdAt.toFormattedDate()
            binding.tvItemDescription.text = story.description
        }

        private fun setupClickListener(story: ListStoryItem) {
            itemView.setOnClickListener {
                val intent = createDetailIntent(story)
                val options = createTransitionOptions()

                itemView.context.startActivity(intent, options.toBundle())
            }
        }

        private fun createDetailIntent(story: ListStoryItem): Intent {
            return Intent(itemView.context, DetailStoryActivity::class.java).apply {
                putExtra(DetailStoryActivity.NAME, story.name)
                putExtra(DetailStoryActivity.CREATE_AT, story.createdAt)
                putExtra(DetailStoryActivity.DESCRIPTION, story.description)
                putExtra(DetailStoryActivity.PHOTO_URL, story.photoUrl)
            }
        }

        private fun createTransitionOptions(): ActivityOptionsCompat {
            return ActivityOptionsCompat.makeSceneTransitionAnimation(
                itemView.context as Activity,
                androidx.core.util.Pair(binding.imgItemPhoto, "photo"),
                androidx.core.util.Pair(binding.tvItemName, "name"),
                androidx.core.util.Pair(binding.tvItemCreated, "createdate"),
                androidx.core.util.Pair(binding.tvItemDescription, "description")
            )
        }
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun getItemCount(): Int = stories.size
}