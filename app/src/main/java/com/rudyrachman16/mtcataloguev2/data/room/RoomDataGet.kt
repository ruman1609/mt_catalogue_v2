package com.rudyrachman16.mtcataloguev2.data.room

import androidx.paging.DataSource
import androidx.sqlite.db.SimpleSQLiteQuery
import com.rudyrachman16.mtcataloguev2.data.room.db.MTDatabase
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities
import com.rudyrachman16.mtcataloguev2.utils.QuerySort

class RoomDataGet private constructor(mtDatabase: MTDatabase) {
    companion object {
        @JvmStatic
        private var INSTANCE: RoomDataGet? = null

        fun getInstance(mtDatabase: MTDatabase): RoomDataGet = INSTANCE ?: synchronized(this) {
            INSTANCE ?: RoomDataGet(mtDatabase).apply { INSTANCE = this }
        }
    }

    private val dao = mtDatabase.getDao()

    suspend fun insertMov(movie: List<MovieEntities>) = dao.insertMov(movie)

    suspend fun insertTv(tvShow: ArrayList<TvShowEntities>) = dao.insertTv(tvShow)


    fun getMov(sort: String): DataSource.Factory<Int, MovieEntities> =
        dao.getMov(
            SimpleSQLiteQuery(
                QuerySort.query(
                    MovieEntities.ENTITY_NAME, MovieEntities.POPULARITY, sort
                )
            )
        )

    suspend fun getMov(): List<MovieEntities> = dao.getMov()

    fun getTv(sort: String): DataSource.Factory<Int, TvShowEntities> =
        dao.getTv(
            SimpleSQLiteQuery(
                QuerySort.query(
                    TvShowEntities.ENTITY_NAME, TvShowEntities.POPULARITY, sort
                )
            )
        )

    suspend fun getTv(): List<TvShowEntities> = dao.getTv()

    fun getFavMov(sort: String): DataSource.Factory<Int, MovieEntities> {
        val query = QuerySort.query(
            MovieEntities.ENTITY_NAME, MovieEntities.POPULARITY, sort, QuerySort.FAV_MOV
        )
        return dao.getFavMov(SimpleSQLiteQuery(query))
    }

    fun getFavTv(sort: String): DataSource.Factory<Int, TvShowEntities> {
        val query = QuerySort.query(
            TvShowEntities.ENTITY_NAME, TvShowEntities.POPULARITY, sort, QuerySort.FAV_TV
        )
        return dao.getFavTv(SimpleSQLiteQuery(query))
    }

    suspend fun updateMovie(movie: MovieEntities) = dao.updateFavMov(movie)

    suspend fun updateTvShow(tvShow: TvShowEntities) = dao.updateFavTv(tvShow)
}