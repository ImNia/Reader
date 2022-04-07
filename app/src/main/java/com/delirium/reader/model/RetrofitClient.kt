package com.delirium.reader.model

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET

object SettingConnect {
    fun getNewsRequest(url: String) : NewsRequest = RetrofitClient.getClient(url).create()
}

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit!!
    }
}

interface NewsRequest {
    @GET(".")
    fun newsFeed(): Call<String>
}