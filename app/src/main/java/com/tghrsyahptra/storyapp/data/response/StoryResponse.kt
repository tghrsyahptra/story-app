package com.tghrsyahptra.storyapp.data.response

import com.google.gson.annotations.SerializedName

/**
 * Represents the response for fetching a list of stories.
 * @property stories The list of stories.
 * @property isError Indicates whether the request was successful.
 * @property message Describes the result of the request.
 */
data class StoryResponse(

    @field:SerializedName("listStory")
    val stories: List<ListStoryItem>,

    @field:SerializedName("error")
    val isError: Boolean,

    @field:SerializedName("message")
    val message: String
)

/**
 * Represents a single story item.
 * @property photoUrl URL of the story's photo.
 * @property createdAt The timestamp when the story was created.
 * @property name The name of the user who posted the story.
 * @property description The description of the story.
 * @property longitude The longitude of the story's location.
 * @property id The unique identifier of the story.
 * @property latitude The latitude of the story's location.
 */
data class ListStoryItem(

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val longitude: Any,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("lat")
    val latitude: Any
)