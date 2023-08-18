package com.diavolo.data.sources.remote.model


import com.google.gson.annotations.SerializedName

data class RemoteAuthorDetails(
    @SerializedName("avatar_path")
    val avatarPath: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("username")
    val username: String
)