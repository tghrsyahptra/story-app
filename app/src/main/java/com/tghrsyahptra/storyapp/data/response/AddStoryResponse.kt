package com.tghrsyahptra.storyapp.data.response

import com.google.gson.annotations.SerializedName

/**
 * Response model for adding a new story.
 * @property error Indicates whether the request was successful or not.
 * @property message The message describing the result of the request.
 */
data class AddStoryResponse(

    @field:SerializedName("error")
    val isError: Boolean,

    @field:SerializedName("message")
    val message: String
)