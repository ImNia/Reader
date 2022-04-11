package com.delirium.reader

import com.delirium.reader.model.NewsFeed

interface CallbackNews {
    fun successfulNews(source: String, news: MutableList<NewsFeed>)
    fun failedNews()
}