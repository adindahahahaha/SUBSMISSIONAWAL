package com.dicoding.picodiploma.loginwithanimation.view.story

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StoryActivity : AppCompatActivity() {

    private lateinit var storyViewModel: StoryViewModel
    private lateinit var adapter: StoryAdapter
    private lateinit var userPreference: UserPreference
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        // Inisialisasi ViewModel dan Adapter
        val factory = ViewModelFactory.getInstance(this)
        storyViewModel = ViewModelProvider(this, factory).get(StoryViewModel::class.java)
        adapter = StoryAdapter()

        // Inisialisasi RecyclerView dan ProgressBar
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewStory)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        progressBar = findViewById(R.id.progressBar) // Menemukan ProgressBar

        // Inisialisasi UserPreference
        userPreference = UserPreference.getInstance(dataStore)

        // Ambil token dan panggil API untuk mendapatkan story
        lifecycleScope.launch {
            val user = userPreference.getSession().first() // Mengambil token dari DataStore
            val token = user.token

            if (token.isNotEmpty()) {
                storyViewModel.getStories(token) // Panggil fungsi di ViewModel
            }

            // Observasi LiveData dari ViewModel
            storyViewModel.stories.observe(this@StoryActivity) { stories ->
                if (stories != null) {
                    adapter.setList(stories) // Set data ke adapter
                }
            }
            storyViewModel.errorMessage.observe(this@StoryActivity) { message ->
                if (message != null) {
                    Toast.makeText(this@StoryActivity, message, Toast.LENGTH_LONG).show()
                }
            }

            // Observasi status loading untuk menunjukkan/hide ProgressBar
            storyViewModel.isLoading.observe(this@StoryActivity) { isLoading ->
                if (isLoading) {
                    progressBar.visibility = View.VISIBLE // Tampilkan ProgressBar
                } else {
                    progressBar.visibility = View.GONE // Sembunyikan ProgressBar
                }
            }
        }
    }
}

