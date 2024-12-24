package com.dicoding.picodiploma.loginwithanimation.view.story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari intent
        val name = intent.getStringExtra(EXTRA_NAME) ?: "No Name"
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: "No Description"
        val photoUrl = intent.getStringExtra(EXTRA_PHOTO_URL)
        val createdAt = intent.getStringExtra(EXTRA_CREATED_AT) ?: "Unknown Date"
        val lat = intent.getDoubleExtra(EXTRA_LAT, Double.NaN)
        val lon = intent.getDoubleExtra(EXTRA_LON, Double.NaN)

        // Tampilkan data ke UI
        binding.tvDetailName.text = name
        binding.tvDetailDescription.text = description
        binding.tvDetailDate.text = getString(R.string.created_at, createdAt)
        binding.tvDetailLocation.text = if (!lat.isNaN() && !lon.isNaN()) {
            getString(R.string.location_format, lat, lon)
        } else {
            getString(R.string.location_not_available)
        }

        Glide.with(this)
            .load(photoUrl)
            .placeholder(R.drawable.default_image) // Tambahkan placeholder agar lebih user-friendly
            .into(binding.ivDetailPhoto)
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_PHOTO_URL = "extra_photo_url"
        const val EXTRA_CREATED_AT = "extra_created_at"
        const val EXTRA_LAT = "extra_lat"
        const val EXTRA_LON = "extra_lon"
    }
}
