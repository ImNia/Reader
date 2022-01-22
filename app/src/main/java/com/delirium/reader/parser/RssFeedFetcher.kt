package com.delirium.reader.parser

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

        return newsFeeds
    }
}