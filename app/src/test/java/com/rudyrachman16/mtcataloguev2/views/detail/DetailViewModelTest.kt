package com.rudyrachman16.mtcataloguev2.views.detail

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rudyrachman16.mtcataloguev2.data.Repositories
import com.rudyrachman16.mtcataloguev2.data.api.models.MovieDetail
import com.rudyrachman16.mtcataloguev2.data.api.models.TvShowDetail
import com.rudyrachman16.mtcataloguev2.utils.DummyData
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    private lateinit var viewModelMv: DetailViewModel
    private lateinit var viewModelTv: DetailViewModel
    private val dummyMovie = DummyData.dummyMovie
    private val dummyTvShow = DummyData.dummyTvShow

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var repositories: Repositories

    @Mock
    private lateinit var movObserver: Observer<MovieDetail>

    @Mock
    private lateinit var tvObserver: Observer<TvShowDetail>

    @Before
    fun setUp() {
        viewModelMv = DetailViewModel(repositories, application, dummyMovie.id)
        viewModelTv = DetailViewModel(repositories, application, dummyTvShow.id)
    }

    @Test
    fun testGetMovie() {
        val movieDetail = MutableLiveData<MovieDetail>()
        movieDetail.value = dummyMovie
        Mockito.`when`(repositories.testMovie(dummyMovie.id)).thenReturn(movieDetail)
        val movie = viewModelMv.testDetail<MovieDetail>(DetailViewModel.MOVIE).value
        Mockito.verify(repositories).testMovie(dummyMovie.id)
        Assert.assertNotNull(movie)
        assertEquals(movie, dummyMovie)

        viewModelMv.testDetail<MovieDetail>(DetailViewModel.MOVIE).observeForever(movObserver)
        Mockito.verify(movObserver).onChanged(dummyMovie)
    }

    @Test
    fun testGetTvShow() {
        val tvShowDetail = MutableLiveData<TvShowDetail>()
        tvShowDetail.value = dummyTvShow
        Mockito.`when`(repositories.testTvShow(dummyTvShow.id)).thenReturn(tvShowDetail)
        val tvShow = viewModelTv.testDetail<TvShowDetail>(DetailViewModel.TV_SHOW).value
        Mockito.verify(repositories).testTvShow(dummyTvShow.id)
        Assert.assertNotNull(tvShow)
        assertEquals(tvShow, dummyTvShow)

        viewModelTv.testDetail<TvShowDetail>(DetailViewModel.TV_SHOW).observeForever(tvObserver)
        Mockito.verify(tvObserver).onChanged(dummyTvShow)
    }
}