package com.rudyrachman16.mtcataloguev2.data.api.models

import com.google.gson.annotations.SerializedName

data class MovieList(
    val id: String,
    val title: String,
    @SerializedName("release_date") val date: String,
    @SerializedName("poster_path") val imgUrl: String,
    @SerializedName("vote_average") val rating: Double,
    @SerializedName("vote_count") val voter: Int,
    val popularity: Double
)