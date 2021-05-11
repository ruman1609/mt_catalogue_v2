package com.rudyrachman16.mtcataloguev2.views.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rudyrachman16.mtcataloguev2.data.Repositories
import com.rudyrachman16.mtcataloguev2.di.Injection
import com.rudyrachman16.mtcataloguev2.views.detail.DetailViewModel
import com.rudyrachman16.mtcataloguev2.views.favorite.tabs.FavoriteTabViewModel
import com.rudyrachman16.mtcataloguev2.views.home.tabs.TabViewModel

class ViewModelFactory
private constructor(private val repositories: Repositories, private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @JvmStatic
        private var instance: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepositories(application.applicationContext), application
                )
            }

        fun getInstance(application: Application, id: String): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepositories(application.applicationContext), application, id
                )
            }
    }

    private constructor(repositories: Repositories, application: Application, id: String) :
            this(repositories, application) {
        this.id = id
    }

    private var id = ""

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(TabViewModel::class.java) ->
                TabViewModel(repositories, application) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) ->
                DetailViewModel(repositories, application, id) as T
            modelClass.isAssignableFrom(FavoriteTabViewModel::class.java) ->
                FavoriteTabViewModel(repositories) as T
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}