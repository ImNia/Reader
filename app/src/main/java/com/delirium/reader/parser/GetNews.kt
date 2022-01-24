package com.delirium.reader.parser

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class GetNews {
    private var stream: InputStream? = null

    fun getDescription(url: URL) : String? {
        val connect = url.openConnection() as HttpURLConnection
        connect.requestMethod = "GET"
        connect.connect()

        val responseCode: Int = connect.responseCode
        var newsFeeds: String? = null
        if(responseCode == 200) {
            stream = connect.inputStream
            newsFeeds = stream!!.bufferedReader().use {
                it.readText()
            }
        }

        return newsFeeds
    }
}