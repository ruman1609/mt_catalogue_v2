package com.rudyrachman16.mtcataloguev2.data.api

import com.rudyrachman16.mtcataloguev2.data.api.models.*
import com.rudyrachman16.mtcataloguev2.utils.ApiCall
import com.rudyrachman16.mtcataloguev2.utils.IdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiDataGet {
    companion object {
        @JvmStatic
        private var instance: ApiDataGet? = null

        fun getInstance(): ApiDataGet =
            instance ?: synchronized(this) {
                instance ?: ApiDataGet().apply { instance = this }
            }
    }

    fun getMovies(callback: ApiCallback<ArrayList<MovieList>>) {
        IdlingResource.increment()
        val response = ApiCall.apiReq.getMovies()
        response.enqueue(object : Callback<MovieItems> {
            override fun onResponse(call: Call<MovieItems>, response: Response<MovieItems>) {
                if (response.isSuccessful) {
                    if (response.body() != null) callback.onSuccess(response.body()!!.list)
                    else callback.onFailure(NullPointerException("Error Empty Tv Show Detail"))
                } else
                    callback.onFailure(Throwable("error ${response.code()}"))
                if (!IdlingResource.idlingResource.isIdleNow) IdlingResource.decrement()
            }

            override fun onFailure(call: Call<MovieItems>, t: Throwable) {
                callback.onFailure(t)
                if (!IdlingResource.idlingResource.isIdleNow) IdlingResource.decrement()
            }
        })
    }

    fun getTvShows(callback: ApiCallback<ArrayList<TvShowList>>) {
        IdlingResource.increment()
        val response = ApiCall.apiReq.getTvShows()
        response.enqueue(object : Callback<TvShowItems> {
            override fun onResponse(call: Call<TvShowItems>, response: Response<TvShowItems>) {
                if (response.isSuccessful) {
                    if (response.body() != null) callback.onSuccess(response.body()!!.list)
                    else callback.onFailure(NullPointerException("Error Empty Tv Show Detail"))
                } else
                    callback.onFailure(Throwable("error ${response.code()}"))
                if (!IdlingResource.idlingResource.isIdleNow) IdlingResource.decrement()
            }

            override fun onFailure(call: Call<TvShowItems>, t: Throwable) {
                callback.onFailure(t)
                if (!IdlingResource.idlingResource.isIdleNow) IdlingResource.decrement()
            }
        })
    }

    fun getTvShow(id: String, callback: ApiCallback<TvShowDetail>) {
        IdlingResource.increment()
        val response = ApiCall.apiReq.getTvShow(id)
        response.enqueue(object : Callback<TvShowDetail> {
            override fun onResponse(call: Call<TvShowDetail>, response: Response<TvShowDetail>) {
                if (response.isSuccessful) {
                    if (response.body() != null) callback.onSuccess(response.body()!!)
                    else callback.onFailure(NullPointerException("Error Empty Tv Show Detail"))
                } else
                    callback.onFailure(Throwable("error ${response.code()}"))
                if (!IdlingResource.idlingResource.isIdleNow) IdlingResource.decrement()
            }

            override fun onFailure(call: Call<TvShowDetail>, t: Throwable) {
                callback.onFailure(t)
                if (!IdlingResource.idlingResource.isIdleNow) IdlingResource.decrement()
            }
        })
    }

    fun getMovie(id: String, callback: ApiCallback<MovieDetail>) {
        IdlingResource.increment()
        val response = ApiCall.apiReq.getMovie(id)
        response.enqueue(object : Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                if (response.isSuccessful) {
                    if (response.body() != null) callback.onSuccess(response.body()!!)
                    else callback.onFailure(NullPointerException("Error Empty Tv Show Detail"))
                } else
                    callback.onFailure(Throwable("error ${response.code()}"))
                if (!IdlingResource.idlingResource.isIdleNow) IdlingResource.decrement()
            }

            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                callback.onFailure(t)
                if (!IdlingResource.idlingResource.isIdleNow) IdlingResource.decrement()
            }
        })
    }
}