package com.rudyrachman16.mtcataloguev2.di

import android.content.Context
import com.rudyrachman16.mtcataloguev2.data.Repositories
import com.rudyrachman16.mtcataloguev2.data.api.ApiDataGet
import com.rudyrachman16.mtcataloguev2.data.room.RoomDataGet
import com.rudyrachman16.mtcataloguev2.data.room.db.MTDatabase

object Injection {
    fun provideRepositories(context: Context): Repositories = Repositories.getInstance(
        ApiDataGet.getInstance(), RoomDataGet.getInstance(MTDatabase.getInstance(context))
    )
}