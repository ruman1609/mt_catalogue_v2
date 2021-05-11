package com.rudyrachman16.mtcataloguev2.utils

import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities

object QuerySort {
    const val ASC = "com.rudyrachman16.mtcataloguev2.utils.ascend"
    const val DSC = "com.rudyrachman16.mtcataloguev2.utils.descend"
    const val RAN = "com.rudyrachman16.mtcataloguev2.utils.random"

    private val ARRAY = arrayOf(DSC, ASC, RAN)

    @JvmStatic
    private var POS = 0

    fun setPos() {
        if (POS == 2) POS = 0
        else POS++
    }

    fun getPos(): Int = POS
    fun getSort(): String = ARRAY[POS]

    private const val SIMPLE = "SELECT * FROM"
    const val FAV_MOV = "WHERE ${MovieEntities.FAVORITE} = 1"
    const val FAV_TV = "WHERE ${TvShowEntities.FAVORITE} = 1"

    fun query(dest: String, table: String, sort: String, favorite: String = ""): String =
        when (sort) {
            ASC -> "$SIMPLE $dest $favorite ORDER BY $table ASC"
            DSC -> "$SIMPLE $dest $favorite ORDER BY $table DESC"
            RAN -> "$SIMPLE $dest $favorite ORDER BY RANDOM()"
            else -> "$SIMPLE $dest $favorite"
        }
}