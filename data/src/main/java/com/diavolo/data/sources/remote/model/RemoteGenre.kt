package com.diavolo.data.sources.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
data class RemoteGenre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
