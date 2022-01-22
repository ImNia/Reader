package com.delirium.reader.parser

import android.util.Log
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class RssFeedFetcher {
    private var stream: InputStream? = null

    fun getTitleNews(url: URL): List<NewsFeed>? {
        val connect = url.openConnection() as HttpURLConnection
        connect.requestMethod = "GET"
        connect.connect()

        val responseCode: Int = connect.responseCode
        var newsFeeds: List<NewsFeed>? = null
        if(responseCode == 200) {
            stream = connect.inputStream
            val parser = RssParser()
            newsFeeds = parser.parse(stream!!)
        }

        Log.i("RSS_FEED_FETCHER", "In function: ${newsFeeds!!.size}")
        return newsFeeds
    }
}