package com.delirium.reader

import com.delirium.reader.model.NewsFeed

interface CallbackNews {
    fun successful(news: List<NewsFeed>)
    fun failed()
}