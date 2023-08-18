package com.diavolo.model


data class ReviewMoviesResponse(
    val id: Int,
    val page: Int,
    val reviews: List<Review>,
    val totalPages: Int,
    val totalResults: Int
)