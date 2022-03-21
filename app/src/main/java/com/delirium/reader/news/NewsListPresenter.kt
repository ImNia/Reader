package com.delirium.reader.news

import android.util.Log
import androidx.lifecycle.ViewModel
import com.delirium.reader.CallbackNews
import com.delirium.reader.model.Model
import com.delirium.reader.model.NewsFeed

class NewsListPresenter : ViewModel(), CallbackNews {
    private var viewNews: NewsList? = null
    private var model = Model(this)

    private var newsList: List<NewsFeed> = listOf()

    init {
        model.getData()
    }

    fun attachView(viewNews: NewsList) {
        this.viewNews = viewNews
    }

    fun currentState() {
        viewNews?.drawNewsList(newsList)
    }

    fun selectNewsTitle(title: String) {
        var selectTitle: NewsFeed? = null
        newsList.forEach { newsFeed ->
            if(newsFeed.title == title)
                selectTitle = newsFeed
        }
        //TODO exception
        viewNews?.selectedNewsTitle(selectTitle!!)
    }

    override fun successful(news: List<NewsFeed>) {
        Log.i("NEWS_LIST", "Data ok: ${model.requestData}")
        newsList = news
        viewNews?.drawNewsList(news)
    }

    override fun failed() {
        // TODO("Not yet implemented")
        Log.i("NEWS_LIST", "Not ok get data: ${model.requestData}")
    }
}