package com.rudyrachman16.mtcataloguev2.utils

import com.rudyrachman16.mtcataloguev2.BuildConfig
import com.rudyrachman16.mtcataloguev2.data.api.ApiRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiCall {
    private val loggingInterceptor =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(loggingInterceptor)
        callTimeout(8, TimeUnit.SECONDS)
        readTimeout(8, TimeUnit.SECONDS)
        connectTimeout(8, TimeUnit.SECONDS)
    }.build()

    val apiReq: ApiRequest by lazy {
        Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            baseUrl(BuildConfig.BASE_URL)
            client(client)
        }.build().create(ApiRequest::class.java)
    }
}
