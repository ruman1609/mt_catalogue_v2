package com.rudyrachman16.mtcataloguev2.data.room.db

import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities

@Dao
interface MTDao {
    @Insert(entity = MovieEntities::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMov(movies: List<MovieEntities>): List<Long>

    @Insert(entity = TvShowEntities::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTv(tvShows: List<TvShowEntities>)

    @Update(entity = MovieEntities::class)
    suspend fun updateFavMov(movie: MovieEntities)

    @Update(entity = TvShowEntities::class)
    suspend fun updateFavTv(tvShow: TvShowEntities)

    @RawQuery(observedEntities = [MovieEntities::class])
    fun getMov(query: SupportSQLiteQuery): DataSource.Factory<Int, MovieEntities>

    @Query("SELECT * FROM movies")
    suspend fun getMov(): List<MovieEntities>

    @RawQuery(observedEntities = [TvShowEntities::class])
    fun getTv(query: SupportSQLiteQuery): DataSource.Factory<Int, TvShowEntities>

    @Query("SELECT * FROM tv_shows")
    suspend fun getTv(): List<TvShowEntities>

    @RawQuery(observedEntities = [MovieEntities::class])
    fun getFavMov(query: SupportSQLiteQuery): DataSource.Factory<Int, MovieEntities>

    @RawQuery(observedEntities = [TvShowEntities::class])
    fun getFavTv(query: SupportSQLiteQuery): DataSource.Factory<Int, TvShowEntities>
}