package com.delirium.reader

import com.delirium.reader.model.NewsFeed
import com.delirium.reader.model.StatusCode

interface CallbackNews {
    fun successfulNews(source: String, news: MutableList<NewsFeed>)
    fun failedNews(statusCode: StatusCode)
}