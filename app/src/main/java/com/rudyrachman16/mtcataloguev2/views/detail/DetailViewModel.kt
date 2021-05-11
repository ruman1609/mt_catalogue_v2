package com.rudyrachman16.mtcataloguev2.views.detail

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rudyrachman16.mtcataloguev2.data.Repositories

class DetailViewModel(
    private val repositories: Repositories,
    private val application: Application,
    private val id: String
) : ViewModel() {
    companion object {
        const val MOVIE = "com.rudyrachman16.mtcatalogueapi.views.detail.movie"
        const val TV_SHOW = "com.rudyrachman16.mtcatalogueapi.views.detail.tv_show"
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getDetail(model: String): LiveData<T> =
        when (model) {
            MOVIE -> {
                repositories.getMovie(id) {
                    Toast.makeText(application.applicationContext, it.message, Toast.LENGTH_LONG)
                        .show()
                } as LiveData<T>
            }
            TV_SHOW -> {
                repositories.getTvShow(id) {
                    Toast.makeText(application.applicationContext, it.message, Toast.LENGTH_LONG)
                        .show()
                } as LiveData<T>
            }
            else -> throw Throwable("Unknown Model class: $model")
        }

    @Suppress("UNCHECKED_CAST")
    fun <T> testDetail(model: String): LiveData<T> =
        when (model) {
            MOVIE -> repositories.testMovie(id) as LiveData<T>
            TV_SHOW -> repositories.testTvShow(id) as LiveData<T>
            else -> throw Throwable("Unknown Model class: $model")
        }

}