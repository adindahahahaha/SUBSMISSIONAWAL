package com.dicoding.picodiploma.loginwithanimation.view.story

import androidx.lifecycle.*
import com.dicoding.picodiploma.loginwithanimation.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> get() = _stories
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>() // Tambahan untuk menampilkan pesan error

    fun getStories(token: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = storyRepository.getStories(token)
                if (response.error == false) {
                    _stories.postValue(response.listStory?.filterNotNull() ?: emptyList())
                } else {
                    errorMessage.postValue("Failed to load stories: ${response.message}")
                    _stories.postValue(emptyList())
                }
            } catch (e: Exception) {
                errorMessage.postValue("An error occurred: ${e.message}")
                _stories.postValue(emptyList())
            } finally {
                isLoading.value = false
            }
        }
    }
}
