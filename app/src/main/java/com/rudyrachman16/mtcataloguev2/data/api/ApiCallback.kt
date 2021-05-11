package com.rudyrachman16.mtcataloguev2.data.api

interface ApiCallback<T> {
    fun onSuccess(item: T)
    fun onFailure(error: Throwable)
}