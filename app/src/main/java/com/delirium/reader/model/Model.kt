package com.delirium.reader.model

import android.util.Log
import com.delirium.reader.CallbackNews
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Model(val callback: CallbackNews) {
    private val newsRequest: NewsRequest = SettingConnect.newsRequest
    var requestData: MutableList<NewsFeed> = mutableListOf()

    fun getData() {
        newsRequest.newsFeed().enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                callback.failed()
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    requestData = parseReceiveNews(response.body())
                    callback.successful(requestData)
                } else {
                    callback.failed()
                }
            }
        })
    }

    private fun parseReceiveNews(body: String?) : MutableList<NewsFeed> {
        val newsList: MutableList<NewsFeed> = mutableListOf()
        val document = Jsoup.parse(body)
        val elements = document.select("item")
        for (item in elements) {
            val newsFeed = NewsFeed()
            for (childNode in item.childNodes()) {
                when(childNode.nodeName()) {
                    "title" -> newsFeed.title = (childNode as Element).text()
                    "link" -> newsFeed.link =
                        (item.childNode(childNode.siblingIndex() + 1) as TextNode).wholeText
                    "description" -> newsFeed.description = (childNode as Element).text()
                }
            }
            newsList.add(newsFeed)
        }
        return newsList
    }
}