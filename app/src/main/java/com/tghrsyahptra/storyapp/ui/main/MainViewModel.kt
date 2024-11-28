package com.tghrsyahptra.storyapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tghrsyahptra.storyapp.data.api.ApiConfig
import com.tghrsyahptra.storyapp.data.response.AddStoryResponse
import com.tghrsyahptra.storyapp.data.response.ListStoryItem
import com.tghrsyahptra.storyapp.data.response.StoryResponse
import com.tghrsyahptra.storyapp.data.response.UserPreferencesManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainViewModel(private val userPreference: UserPreferencesManager) : ViewModel() {

    // LiveData for stories and loading state
    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Fetches a list of stories from the API.
     * @param token The user's authorization token.
     */
    fun fetchStories(token: String) {
        setLoadingState(true)
        val client = ApiConfig.getApiService().getStories("Bearer $token")
        client.enqueue(createStoryCallback())
    }

    /**
     * Uploads a new story.
     * @param token The user's authorization token.
     * @param imageFile The image file for the story.
     * @param description The description of the story.
     */
    fun uploadStory(token: String, imageFile: File, description: String) {
        setLoadingState(true)

        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val imageBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("photo", imageFile.name, imageBody)

        val client = ApiConfig.getApiService().uploadStory("Bearer $token", imagePart, descriptionBody)
        client.enqueue(createUploadStoryCallback())
    }

    /**
     * Creates a callback for fetching stories from the API.
     * @return Callback for the StoryResponse.
     */
    private fun createStoryCallback(): Callback<StoryResponse> {
        return object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                setLoadingState(false)
                if (response.isSuccessful) {
                    _stories.value = response.body()?.stories
                } else {
                    logApiError("Error fetching stories", response)
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                setLoadingState(false)
                Log.e(TAG, "Failed to fetch stories: ${t.message}")
            }
        }
    }

    /**
     * Creates a callback for uploading a new story.
     * @return Callback for the AddStoryResponse.
     */
    private fun createUploadStoryCallback(): Callback<AddStoryResponse> {
        return object : Callback<AddStoryResponse> {
            override fun onResponse(call: Call<AddStoryResponse>, response: Response<AddStoryResponse>) {
                setLoadingState(false)
                if (!response.isSuccessful) {
                    logApiError("Error uploading new story", response)
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                setLoadingState(false)
                Log.e(TAG, "Failed to upload story: ${t.message}")
            }
        }
    }

    /**
     * Logs API errors for debugging.
     * @param message Custom error message.
     * @param response The HTTP response.
     */
    private fun logApiError(message: String, response: Response<*>) {
        val errorDetails = "${response.code()} : ${response.message()}"
        Log.e(TAG, "$message - $errorDetails")
    }

    /**
     * Sets the loading state.
     * @param isLoading The loading state to be set.
     */
    private fun setLoadingState(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}