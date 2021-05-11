package com.rudyrachman16.mtcataloguev2.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.rudyrachman16.mtcataloguev2.data.api.ApiCallback
import com.rudyrachman16.mtcataloguev2.data.api.ApiDataGet
import com.rudyrachman16.mtcataloguev2.data.api.models.MovieDetail
import com.rudyrachman16.mtcataloguev2.data.api.models.MovieList
import com.rudyrachman16.mtcataloguev2.data.api.models.TvShowDetail
import com.rudyrachman16.mtcataloguev2.data.api.models.TvShowList
import com.rudyrachman16.mtcataloguev2.data.room.RoomDataGet
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities
import com.rudyrachman16.mtcataloguev2.utils.IdlingResource
import com.rudyrachman16.mtcataloguev2.utils.QuerySort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class Repositories private constructor(
    private val apiDataGet: ApiDataGet,
    private val roomDataGet: RoomDataGet
) : CoroutineScope {
    companion object {
        @JvmStatic
        private var instance: Repositories? = null

        fun getInstance(apiDataGet: ApiDataGet, roomDataGet: RoomDataGet): Repositories =
            instance ?: synchronized(this) {
                instance ?: Repositories(apiDataGet, roomDataGet).apply { instance = this }
            }

        @JvmStatic
        var firstMovie = true

        @JvmStatic
        var firstTvShow = true
    }

    fun getMovies(sort: String, failed: (error: Throwable) -> Unit):
            LiveData<PagedList<MovieEntities>> {
        var mediator = MediatorLiveData<PagedList<MovieEntities>>()
        apiDataGet.getMovies(object : ApiCallback<ArrayList<MovieList>> {
            override fun onSuccess(item: ArrayList<MovieList>) {
                IdlingResource.increment()
                val job = launch(Dispatchers.IO) {
                    val list = roomDataGet.getMov()
                    if (list.isNotEmpty()) {
                        if (list.toString().replace("MovieEntities", "") != item.toString()
                                .replace("MovieList", "")
                        )
                            iuDB(item, item[0], list)
                    } else iuDB<MovieList, MovieEntities>(item, item[0], null)
                }
                launch(coroutineContext) {
                    if (firstMovie) {
                        job.join()
                        firstMovie = false
                    }
                    if (job.isCompleted || !firstMovie) {
                        mediator = getFromDB(mediator, declareLiveData(roomDataGet.getMov(sort)))
                    }
                }
            }

            override fun onFailure(error: Throwable) {
                failed(error)
                mediator = getFromDB(mediator, declareLiveData(roomDataGet.getMov(sort)))
            }
        })
        return mediator
    }

    fun testMovies(): LiveData<PagedList<MovieEntities>> {
        var mediator = MediatorLiveData<PagedList<MovieEntities>>()
        apiDataGet.getMovies(object :
            ApiCallback<ArrayList<MovieList>> {
            override fun onSuccess(item: ArrayList<MovieList>) {
                val job = launch(Dispatchers.IO) {
                    val list = roomDataGet.getMov()
                    if (list.isNotEmpty()) {
                        if (list.toString().replace("MovieEntities", "") != item.toString()
                                .replace("MovieList", "")
                        )
                            iuDB(item, item[0], list)
                    } else iuDB<MovieList, MovieEntities>(item, item[0], null)
                }
                launch(coroutineContext) {
                    if (firstMovie) {
                        job.join()
                        firstMovie = false
                    }
                    if (job.isCompleted || !firstMovie)
                        mediator =
                            getFromDB(mediator, declareLiveData(roomDataGet.getMov(QuerySort.DSC)))
                }
            }

            override fun onFailure(error: Throwable) {
                mediator = getFromDB(mediator, declareLiveData(roomDataGet.getMov(QuerySort.DSC)))
            }
        })
        return mediator
    }

    fun getTvShows(
        sort: String, failed: (error: Throwable) -> Unit
    ): LiveData<PagedList<TvShowEntities>> {
        var mediator = MediatorLiveData<PagedList<TvShowEntities>>()
        IdlingResource.increment()
        apiDataGet.getTvShows(object : ApiCallback<ArrayList<TvShowList>> {
            override fun onSuccess(item: ArrayList<TvShowList>) {
                val job = launch(Dispatchers.IO) {
                    val list = roomDataGet.getTv()
                    if (list.isNotEmpty()) {
                        if (list.toString().replace("MovieEntities", "") != item.toString()
                                .replace("MovieList", "")
                        )
                            iuDB(item, item[0], list)
                    } else iuDB<TvShowList, TvShowEntities>(item, item[0], null)
                }
                launch(coroutineContext) {
                    if (firstTvShow) {
                        job.join()
                        firstTvShow = false
                    }
                    if (job.isCompleted || !firstTvShow) {
                        mediator = getFromDB(mediator, declareLiveData(roomDataGet.getTv(sort)))
                    }
                }
            }

            override fun onFailure(error: Throwable) {
                failed(error)
                mediator = getFromDB(mediator, declareLiveData(roomDataGet.getTv(sort)))
            }
        })
        return mediator
    }

    fun testTvShows(): LiveData<PagedList<TvShowEntities>> {
        var mediator = MediatorLiveData<PagedList<TvShowEntities>>()
        apiDataGet.getTvShows(object : ApiCallback<ArrayList<TvShowList>> {
            override fun onSuccess(item: ArrayList<TvShowList>) {
                val job = launch(Dispatchers.IO) {
                    val list = roomDataGet.getTv()
                    if (list.isNotEmpty()) {
                        if (list.toString().replace("MovieEntities", "") != item.toString()
                                .replace("MovieList", "")
                        )
                            iuDB(item, item[0], list)
                    } else iuDB<TvShowList, TvShowEntities>(item, item[0], null)
                }
                launch(coroutineContext) {
                    if (firstTvShow) {
                        job.join()
                        firstTvShow = false
                    }
                    if (job.isCompleted || !firstTvShow)
                        mediator =
                            getFromDB(mediator, declareLiveData(roomDataGet.getTv(QuerySort.DSC)))
                }
            }

            override fun onFailure(error: Throwable) {
                mediator = getFromDB(mediator, declareLiveData(roomDataGet.getTv(QuerySort.DSC)))
            }
        })
        return mediator
    }

    fun getFavMovies(sort: String): LiveData<PagedList<MovieEntities>> =
        declareLiveData(roomDataGet.getFavMov(sort))

    fun testFavMovies(): LiveData<PagedList<MovieEntities>> =
        declareLiveData(roomDataGet.getFavMov(QuerySort.DSC))


    fun getFavTv(sort: String): LiveData<PagedList<TvShowEntities>> =
        declareLiveData(roomDataGet.getFavTv(sort))

    fun testFavTv(): LiveData<PagedList<TvShowEntities>> =
        declareLiveData(roomDataGet.getFavTv(QuerySort.DSC))

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

    fun testMovie(id: String): LiveData<MovieDetail> {
        val movieDetail = MutableLiveData<MovieDetail>()
        apiDataGet.getMovie(id, object : ApiCallback<MovieDetail> {
            override fun onSuccess(item: MovieDetail) {
                movieDetail.postValue(item)
            }

            override fun onFailure(error: Throwable) {}
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

    fun testTvShow(id: String): LiveData<TvShowDetail> {
        val tvShowDetail = MutableLiveData<TvShowDetail>()
        apiDataGet.getTvShow(id, object : ApiCallback<TvShowDetail> {
            override fun onSuccess(item: TvShowDetail) {
                tvShowDetail.postValue(item)
            }

            override fun onFailure(error: Throwable) {}
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

    private fun <T> getFromDB(
        mediator: MediatorLiveData<T>, source: LiveData<T>
    ): MediatorLiveData<T> {
        mediator.addSource(source) { mediator.value = it }
        return mediator
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T, U> iuDB(data: ArrayList<T>, model: T, update: List<U>?) {
        launch(coroutineContext) {
            withContext(Dispatchers.IO) {
                when (model) {
                    is MovieList -> {
                        val list = ArrayList<MovieEntities>()
                        data.mapIndexed { index, it ->
                            it as MovieList
                            val up = (update?.get(index) as MovieEntities?)?.favorite
                            list.add(
                                MovieEntities(
                                    it.id, it.title, it.date, it.imgUrl,
                                    it.rating, it.voter, it.popularity, up ?: false
                                )
                            )
                        }
                        roomDataGet.insertMov(list)
                    }
                    is TvShowList -> {
                        val list = ArrayList<TvShowEntities>()
                        data.mapIndexed { index, it ->
                            it as TvShowList
                            val up = (update?.get(index) as TvShowEntities?)?.favorite
                            list.add(
                                TvShowEntities(
                                    it.id, it.title, it.date, it.imgUrl,
                                    it.rating, it.voter, it.popularity, up ?: false
                                )
                            )
                        }
                        roomDataGet.insertTv(list)
                    }
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}