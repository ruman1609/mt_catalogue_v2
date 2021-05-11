package com.rudyrachman16.mtcataloguev2.data.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tv_shows")
@Parcelize
data class TvShowEntities(
    @PrimaryKey @ColumnInfo(name = "t_id") val id: String,
    @ColumnInfo(name = "t_title") val title: String,
    @ColumnInfo(name = "t_date") val date: String,
    @ColumnInfo(name = "t_imgUrl") val imgUrl: String,
    @ColumnInfo(name = "t_rating") val rating: Double,
    @ColumnInfo(name = "t_voter") val voter: Int,
    @ColumnInfo(name = "t_popularity") val popularity: Double,
    @ColumnInfo(name = "t_favorite") var favorite: Boolean
) : Parcelable {
    companion object {
        const val ENTITY_NAME = "tv_shows"
        const val POPULARITY = "t_popularity"
        const val FAVORITE = "t_favorite"
    }
}
