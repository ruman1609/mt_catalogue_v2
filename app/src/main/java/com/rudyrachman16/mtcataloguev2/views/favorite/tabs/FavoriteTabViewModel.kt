package com.rudyrachman16.mtcataloguev2.views.favorite.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.rudyrachman16.mtcataloguev2.data.Repositories
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities
import com.rudyrachman16.mtcataloguev2.utils.QuerySort

class FavoriteTabViewModel(private val repositories: Repositories) : ViewModel() {

    private var sort = MutableLiveData<String>()

    fun setSort() {
        sort.value = QuerySort.getSort()
    }

    fun getSort(): LiveData<String> = sort

    fun getMovies(): LiveData<PagedList<MovieEntities>> = repositories.getFavMovies(sort.value!!)
    fun testMovies(): LiveData<PagedList<MovieEntities>> = repositories.testFavMovies()

    fun getTvShow(): LiveData<PagedList<TvShowEntities>> = repositories.getFavTv(sort.value!!)
    fun testTvShow(): LiveData<PagedList<TvShowEntities>> = repositories.testFavTv()

    fun setFavoriteMov(movie: MovieEntities) = repositories.setFavoriteMov(movie)

    fun setFavoriteTv(tvShow: TvShowEntities) = repositories.setFavoriteTv(tvShow)
}