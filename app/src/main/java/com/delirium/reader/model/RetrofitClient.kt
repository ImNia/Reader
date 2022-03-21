package com.delirium.reader.model

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET

object SettingConnect {
    private const val BASE_URL = "https://lenta.ru/"
    val newsRequest: NewsRequest
        get() = RetrofitClient.getClient(BASE_URL).create()
}

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

interface NewsRequest {
    @GET("rss/news")
    fun newsFeed(): Call<String>
}