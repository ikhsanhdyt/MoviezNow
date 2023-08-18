package com.diavolo.data.sources.remote.model


import com.google.gson.annotations.SerializedName

data class RemoteReviewListResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val reviews: List<RemoteReview>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)