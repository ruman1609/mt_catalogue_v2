package com.rudyrachman16.mtcataloguev2.data.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities

@Database(
    entities = [MovieEntities::class, TvShowEntities::class],
    version = 1, exportSchema = false
)
abstract class MTDatabase : RoomDatabase() {
    companion object {
        @Volatile
        var INSTANCE: MTDatabase? = null

        fun getInstance(context: Context): MTDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context, MTDatabase::class.java, "mt_database")
                .fallbackToDestructiveMigration().build().apply { INSTANCE = this }
        }
    }

    abstract fun getDao(): MTDao
}