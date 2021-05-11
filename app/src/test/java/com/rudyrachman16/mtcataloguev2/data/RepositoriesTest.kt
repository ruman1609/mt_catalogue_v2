package com.rudyrachman16.mtcataloguev2.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.rudyrachman16.mtcataloguev2.data.api.ApiCallback
import com.rudyrachman16.mtcataloguev2.data.api.ApiDataGet
import com.rudyrachman16.mtcataloguev2.data.api.models.MovieDetail
import com.rudyrachman16.mtcataloguev2.data.api.models.TvShowDetail
import com.rudyrachman16.mtcataloguev2.data.room.RoomDataGet
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities
import com.rudyrachman16.mtcataloguev2.utils.DummyData
import com.rudyrachman16.mtcataloguev2.utils.LiveDataTestUtil
import com.rudyrachman16.mtcataloguev2.utils.PagedListUtil
import com.rudyrachman16.mtcataloguev2.utils.QuerySort
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@Suppress("UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
class RepositoriesTest {
    private val movieList = DummyData.dummyMovEnt()
    private val tvShowList = DummyData.dummyTvEnt()
    private val dummyMovie = DummyData.dummyMovie
    private val dummyTvShow = DummyData.dummyTvShow

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val apiDataGet = mock(ApiDataGet::class.java)
    private val roomDataGet = mock(RoomDataGet::class.java)

    private val repositories = FakeRepositories(apiDataGet, roomDataGet)

    @Test
    fun testMoviePagedList() {
        val movieEnt = PagedListUtil.mockPagedList(movieList)
        assertNotNull(movieEnt)
        assertNotNull(movieEnt[0])
        assertEquals(movieEnt.size, movieList.size)
    }

    @Test
    fun testTvShowPagedList() {
        val tvShowEnt = PagedListUtil.mockPagedList(tvShowList)
        assertNotNull(tvShowEnt)
        assertNotNull(tvShowEnt[0])
        assertEquals(tvShowEnt.size, tvShowList.size)
    }

    @Test
    fun getFavMov() {
        val dataSource =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntities>
        `when`(roomDataGet.getFavMov(QuerySort.DSC)).thenReturn(dataSource)
        repositories.getFavMovies(QuerySort.DSC)
        verify(roomDataGet).getFavMov(QuerySort.DSC)
    }

    @Test
    fun getFavTv() {
        val dataSource =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntities>
        `when`(roomDataGet.getFavTv(QuerySort.DSC)).thenReturn(dataSource)
        repositories.getFavTv(QuerySort.DSC)
        verify(roomDataGet).getFavTv(QuerySort.DSC)
    }

    @Test
    fun getMovie() {
        doAnswer {
            (it.arguments[1] as ApiCallback<MovieDetail>).apply {
                onSuccess(dummyMovie)
                onFailure(NullPointerException("Error"))
            }
            null
        }.`when`(apiDataGet).getMovie(eq(dummyMovie.id), any())
        val movie = LiveDataTestUtil.getValue(repositories.getMovie(dummyMovie.id) {})

        verify(apiDataGet).getMovie(eq(dummyMovie.id), any())
        assertNotNull(movie)
        assertEquals(movie, dummyMovie)
    }

    @Test
    fun getTvShow() {
        doAnswer {
            (it.arguments[1] as ApiCallback<TvShowDetail>).apply {
                onSuccess(dummyTvShow)
                onFailure(NullPointerException("Error"))
            }
            null
        }.`when`(apiDataGet).getTvShow(eq(dummyTvShow.id), any())
        val tvShow = LiveDataTestUtil.getValue(repositories.getTvShow(dummyTvShow.id) {})

        verify(apiDataGet).getTvShow(eq(dummyTvShow.id), any())
        assertNotNull(tvShow)
        assertEquals(tvShow, dummyTvShow)
    }

    @Test
    fun setUnSetFavorite() = runBlocking {
        val movie = movieList[0]
        `when`(roomDataGet.updateMovie(movie)).thenReturn(Unit)
        repositories.setFavoriteMov(movie)
        verify(roomDataGet).updateMovie(movie)
        assertEquals(true, movie.favorite)

        repositories.setFavoriteMov(movie)
        roomDataGet.updateMovie(movie)
        assertEquals(false, movie.favorite)

        val tvShow = tvShowList[0]
        `when`(roomDataGet.updateTvShow(tvShow)).thenReturn(Unit)
        repositories.setFavoriteTv(tvShow)
        verify(roomDataGet).updateTvShow(tvShow)
        assertEquals(true, tvShow.favorite)

        repositories.setFavoriteTv(tvShow)
        roomDataGet.updateTvShow(tvShow)
        assertEquals(false, tvShow.favorite)
    }
}