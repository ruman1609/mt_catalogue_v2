package com.rudyrachman16.mtcataloguev2.views.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.rudyrachman16.mtcataloguev2.data.Repositories
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities
import com.rudyrachman16.mtcataloguev2.utils.DummyData
import com.rudyrachman16.mtcataloguev2.views.favorite.tabs.FavoriteTabViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoriteTabViewModelTest {
    private lateinit var viewModel: FavoriteTabViewModel

    @Mock
    private lateinit var repositories: Repositories

    @Mock
    private lateinit var movObs: Observer<PagedList<MovieEntities>>

    @Mock
    private lateinit var tvObs: Observer<PagedList<TvShowEntities>>

    @Mock
    private lateinit var movList: PagedList<MovieEntities>

    @Mock
    private lateinit var tvList: PagedList<TvShowEntities>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = FavoriteTabViewModel(repositories)
    }

    @Test
    fun testMovies() {
        val pagedList = movList
        `when`(pagedList.size).thenReturn(20)
        val liveData = MutableLiveData<PagedList<MovieEntities>>()
        liveData.value = pagedList

        `when`(repositories.testFavMovies()).thenReturn(liveData)
        val movies = viewModel.testMovies().value
        verify(repositories).testFavMovies()
        assertNotNull(movies)
        assertEquals(20, movies?.size)

        viewModel.testMovies().observeForever(movObs)
        verify(movObs).onChanged(pagedList)
    }

    @Test
    fun testTvShow() {
        val pagedList = tvList
        `when`(pagedList.size).thenReturn(20)
        val liveData = MutableLiveData<PagedList<TvShowEntities>>()
        liveData.value = pagedList

        `when`(repositories.testFavTv()).thenReturn(liveData)
        val tvShow = viewModel.testTvShow().value
        verify(repositories).testFavTv()
        assertNotNull(tvShow)
        assertEquals(20, tvShow?.size)

        viewModel.testTvShow().observeForever(tvObs)
        verify(tvObs).onChanged(pagedList)
    }

    @Test
    fun testSetFavMovie() {
        val movie = DummyData.dummyMovEnt()[0]
        doNothing().`when`(repositories).setFavoriteMov(movie)
        viewModel.setFavoriteMov(movie)
        verify(repositories).setFavoriteMov(movie)
    }

    @Test
    fun testSetFavTvShow() {
        val tvShow = DummyData.dummyTvEnt()[0]
        doNothing().`when`(repositories).setFavoriteTv(tvShow)
        viewModel.setFavoriteTv(tvShow)
        verify(repositories).setFavoriteTv(tvShow)
    }
}