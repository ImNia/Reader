package com.delirium.reader

import com.delirium.reader.model.NewsFeed

interface CallbackNews {
    fun successful(source: String, news: MutableList<NewsFeed>)
    fun failed()
}