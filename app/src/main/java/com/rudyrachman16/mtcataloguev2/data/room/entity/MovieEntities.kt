package com.rudyrachman16.mtcataloguev2.data.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "movies")
@Parcelize
data class MovieEntities(
    @PrimaryKey @ColumnInfo(name = "m_id") val id: String,
    @ColumnInfo(name = "m_title") val title: String,
    @ColumnInfo(name = "m_date") val date: String,
    @ColumnInfo(name = "m_imgUrl") val imgUrl: String,
    @ColumnInfo(name = "m_rating") val rating: Double,
    @ColumnInfo(name = "m_voter") val voter: Int,
    @ColumnInfo(name = "m_popularity") val popularity: Double,
    @ColumnInfo(name = "m_favorite") var favorite: Boolean
) : Parcelable {
    companion object {
        const val ENTITY_NAME = "movies"
        const val POPULARITY = "m_popularity"
        const val FAVORITE = "m_favorite"
    }
}
