package com.rudyrachman16.mtcataloguev2.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.rudyrachman16.mtcataloguev2.data.api.ApiCallback
import com.rudyrachman16.mtcataloguev2.data.api.ApiDataGet
import com.rudyrachman16.mtcataloguev2.data.api.models.MovieDetail
import com.rudyrachman16.mtcataloguev2.data.api.models.TvShowDetail
import com.rudyrachman16.mtcataloguev2.data.room.RoomDataGet
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FakeRepositories(
    private val apiDataGet: ApiDataGet,
    private val roomDataGet: RoomDataGet
) : CoroutineScope {

    fun getFavMovies(sort: String): LiveData<PagedList<MovieEntities>> =
        declareLiveData(roomDataGet.getFavMov(sort))

    fun getFavTv(sort: String): LiveData<PagedList<TvShowEntities>> =
        declareLiveData(roomDataGet.getFavTv(sort))

    fun getMovie(id: String, failed: (error: Throwable) -> Unit): LiveData<MovieDetail> {
        val movieDetail = MutableLiveData<MovieDetail>()
        apiDataGet.getMovie(id, object : ApiCallback<MovieDetail> {
            override fun onSuccess(item: MovieDetail) {
                movieDetail.postValue(item)
            }

            override fun onFailure(error: Throwable) {
                failed(error)
            }
        })
        return movieDetail
    }

    fun getTvShow(id: String, failed: (error: Throwable) -> Unit): LiveData<TvShowDetail> {
        val tvShowDetail = MutableLiveData<TvShowDetail>()
        apiDataGet.getTvShow(id, object : ApiCallback<TvShowDetail> {
            override fun onSuccess(item: TvShowDetail) {
                tvShowDetail.postValue(item)
            }

            override fun onFailure(error: Throwable) {
                failed(error)
            }
        })
        return tvShowDetail
    }

    fun setFavoriteMov(movie: MovieEntities) {
        launch(Dispatchers.IO) {
            movie.favorite = !movie.favorite
            roomDataGet.updateMovie(movie)
        }
    }

    fun setFavoriteTv(tvShow: TvShowEntities) {
        launch(Dispatchers.IO) {
            tvShow.favorite = !tvShow.favorite
            roomDataGet.updateTvShow(tvShow)
        }
    }

    private fun <T> declareLiveData(data: DataSource.Factory<Int, T>): LiveData<PagedList<T>> {
        val config = PagedList.Config.Builder().apply {
            setEnablePlaceholders(false)
            setInitialLoadSizeHint(3)
            setPageSize(3)
        }.build()
        return LivePagedListBuilder(data, config).build()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}