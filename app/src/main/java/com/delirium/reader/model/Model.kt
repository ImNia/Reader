package com.delirium.reader.model

import com.delirium.reader.CallbackNews
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class Model(val callback: CallbackNews) {
    private lateinit var newsRequest: NewsRequest
    var requestData: MutableList<NewsFeed> = mutableListOf()

    fun getData(urlNews: String) {
        newsRequest = SettingConnect.getNewsRequest(urlNews)
        newsRequest.newsFeed().enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                when (t) {
                    is SocketTimeoutException ->
                        callback.failedNews(StatusCode.REQUEST_TIMEOUT)
                    is NumberFormatException ->
                        callback.failedNews(StatusCode.CONFLICT_VALUE)
                    is UnknownHostException ->
                        callback.failedNews(StatusCode.NOT_CONNECT)
                    else -> callback.failedNews(StatusCode.SOME_ERROR)
                }
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    requestData = parseReceiveNews(response.body(), urlNews)
                    callback.successfulNews(urlNews, requestData)
                } else {
                    callback.failedNews(StatusCode.NOT_FOUND)
                }
            }
        })
    }

    private fun parseReceiveNews(body: String?, source: String) : MutableList<NewsFeed> {
        val newsList: MutableList<NewsFeed> = mutableListOf()
        val document = Jsoup.parse(body)
        val elements = document.select("item")
        for (item in elements) {
            val newsFeed = NewsFeed()
            for (childNode in item.childNodes()) {
                when(childNode.nodeName()) {
                    "guid" -> newsFeed.guid = (childNode as Element).text()
                    "title" -> newsFeed.title = (childNode as Element).text()
                    "link" -> newsFeed.link =
                        (item.childNode(childNode.siblingIndex() + 1) as TextNode).wholeText
                    "description" -> newsFeed.description = (childNode as Element).text()
                    "pubDate", "pubdate" -> newsFeed.releaseDate = (childNode as Element).text()
                }
                newsFeed.source = source
            }
            newsList.add(newsFeed)
        }
        return newsList
    }
}