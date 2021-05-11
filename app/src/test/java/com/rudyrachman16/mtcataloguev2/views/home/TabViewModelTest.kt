package com.rudyrachman16.mtcataloguev2.views.home

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.rudyrachman16.mtcataloguev2.data.Repositories
import com.rudyrachman16.mtcataloguev2.data.room.entity.MovieEntities
import com.rudyrachman16.mtcataloguev2.data.room.entity.TvShowEntities
import com.rudyrachman16.mtcataloguev2.utils.ApiCall
import com.rudyrachman16.mtcataloguev2.utils.DummyData
import com.rudyrachman16.mtcataloguev2.views.home.tabs.TabViewModel
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class TabViewModelTest {
    private lateinit var viewModel: TabViewModel

    @Mock
    private lateinit var repositories: Repositories

    @Mock
    private lateinit var application: Application

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

    private lateinit var mockServer: MockWebServer

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = TabViewModel(repositories, application)

        mockServer = MockWebServer()
        mockServer.start()
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun testApiCall() {
        val response = MockResponse().apply {
            setResponseCode(HttpURLConnection.HTTP_OK)
            setBody(DummyData.jsonMovie)
        }
        mockServer.enqueue(response)

        val actual = ApiCall.apiReq.getMovies()
        assertEquals(
            response.toString().contains("results"),
            actual.toString().contains("results")
        )
    }

    @Test
    fun getMovies() {  // same like Favorite Tab View Model too
        val pagedList = movList
        `when`(pagedList.size).thenReturn(20)
        val liveData = MutableLiveData<PagedList<MovieEntities>>()
        liveData.value = pagedList

        `when`(repositories.testMovies()).thenReturn(liveData)
        val movies = viewModel.testMovies().value
        verify(repositories).testMovies()
        Assert.assertNotNull(movies)
        assertEquals(20, movies?.size)

        viewModel.testMovies().observeForever(movObs)
        verify(movObs).onChanged(pagedList)
    }

    @Test
    fun getTvShows() {  // same like Favorite Tab View Model too
        val pagedList = tvList
        `when`(pagedList.size).thenReturn(20)
        val liveData = MutableLiveData<PagedList<TvShowEntities>>()
        liveData.value = pagedList

        `when`(repositories.testTvShows()).thenReturn(liveData)
        val tvShow = viewModel.testTvShow().value
        verify(repositories).testTvShows()
        Assert.assertNotNull(tvShow)
        assertEquals(20, tvShow?.size)

        viewModel.testTvShow().observeForever(tvObs)
        verify(tvObs).onChanged(pagedList)
    }
}