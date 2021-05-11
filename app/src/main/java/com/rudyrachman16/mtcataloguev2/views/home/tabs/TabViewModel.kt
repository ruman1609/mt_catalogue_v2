package com.rudyrachman16.mtcataloguev2.views.home.tabs

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.rudyrachman16.mtcataloguev2.data.Repositories
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities
import com.rudyrachman16.mtcataloguev2.utils.QuerySort

class TabViewModel(private val repositories: Repositories, private val application: Application) :
    ViewModel() {

    private var sort = MutableLiveData<String>()

    fun setSort() {
        sort.value = QuerySort.getSort()
    }

    fun getSort(): LiveData<String> = sort

    fun getMovies(): LiveData<PagedList<MovieEntities>> = repositories.getMovies(sort.value!!) {
        Toast.makeText(application.applicationContext, it.message, Toast.LENGTH_LONG).show()
    }

    fun testMovies(): LiveData<PagedList<MovieEntities>> = repositories.testMovies()

    fun getTvShow(): LiveData<PagedList<TvShowEntities>> = repositories.getTvShows(sort.value!!) {
        Toast.makeText(application.applicationContext, it.message, Toast.LENGTH_LONG).show()
    }

    fun testTvShow(): LiveData<PagedList<TvShowEntities>> = repositories.testTvShows()
}