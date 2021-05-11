package com.rudyrachman16.mtcataloguev2.data.api.models

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    val id: String,
    val title: String,
    @SerializedName("overview") val desc: String,
    @SerializedName("release_date") val date: String,
    @SerializedName("poster_path") val imgUrl: String,
    @SerializedName("backdrop_path") val bgUrl: String,
    @SerializedName("vote_average") val rating: Double,
    @SerializedName("vote_count") val voter: Int,
    @SerializedName("production_companies") val companies: ArrayList<Company>,
    val genres: ArrayList<Genre>
)