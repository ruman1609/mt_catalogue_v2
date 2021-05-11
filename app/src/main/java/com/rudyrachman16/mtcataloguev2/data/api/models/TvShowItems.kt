package com.rudyrachman16.mtcataloguev2.data.api.models

import com.google.gson.annotations.SerializedName

data class TvShowItems(
    @SerializedName("results") val list: ArrayList<TvShowList>
)
