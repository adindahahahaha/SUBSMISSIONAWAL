package com.dicoding.picodiploma.loginwithanimation.view.story

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemStoryBinding

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private val storyList = mutableListOf<ListStoryItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int = storyList.size

    fun setList(stories: List<ListStoryItem>) {
        storyList.clear()
        storyList.addAll(stories)
        notifyDataSetChanged()
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvName.text = story.name
            binding.tvDescription.text = story.description
            Glide.with(binding.ivPhoto.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.default_image) // Tambahkan gambar default
                .error(R.drawable.default_image) // Tambahkan gambar error
                .into(binding.ivPhoto)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.EXTRA_NAME, story.name)
                    putExtra(DetailStoryActivity.EXTRA_DESCRIPTION, story.description)
                    putExtra(DetailStoryActivity.EXTRA_PHOTO_URL, story.photoUrl)
                    putExtra(DetailStoryActivity.EXTRA_CREATED_AT, story.createdAt)
                    putExtra(DetailStoryActivity.EXTRA_LAT, story.lat)
                    putExtra(DetailStoryActivity.EXTRA_LON, story.lon)
                }
                context.startActivity(intent)
            }
        }
    }
}

