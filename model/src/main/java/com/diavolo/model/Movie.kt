package com.diavolo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
@Parcelize
data class Movie(
    val popularity: Double,
    val vote_count: Int,
    val video: Boolean,
    val poster_path: String,
    val id: Int,
    val adult: Boolean,
    val backdrop_path: String,
    val original_language: String,
    val original_title: String,
    val title: String,
    val vote_average: Double,
    val overview: String,
    val release_date: String
):Parcelable