package com.rudyrachman16.mtcataloguev2.data.api.models

import com.google.gson.annotations.SerializedName

data class TvShowDetail(
    val id: String,
    @SerializedName("name") val title: String,
    @SerializedName("overview") val desc: String,
    @SerializedName("first_air_date") val date: String,
    val status: String,
    @SerializedName("poster_path") val imgUrl: String,
    @SerializedName("backdrop_path") val bgUrl: String,
    @SerializedName("vote_average") val rating: Double,
    @SerializedName("vote_count") val voter: Int,
    @SerializedName("number_of_episodes") val episodes: Int,
    @SerializedName("number_of_seasons") val seasons: Int,
    @SerializedName("production_companies") val companies: ArrayList<Company>,
    val genres: ArrayList<Genre>
)