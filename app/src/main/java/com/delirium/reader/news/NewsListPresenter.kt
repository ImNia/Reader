package com.delirium.reader.news

import android.util.Log
import androidx.lifecycle.ViewModel
import com.delirium.reader.CallbackNews
import com.delirium.reader.model.Model
import com.delirium.reader.model.NewsFeed
import com.delirium.reader.sources.Source

class NewsListPresenter : ViewModel(), CallbackNews {
    private var viewNews: NewsList? = null
    private var model = Model(this)

    private var newsList: MutableList<NewsFeed> = mutableListOf()
    private var newsFromAllResource: HashMap<String, List<NewsFeed>> = hashMapOf()
//    private var sourceName: String? = null

    fun attachViewAndInit(viewNews: NewsList, sources: List<Source>) {
        this.viewNews = viewNews
        newsList.clear()
        for (source in sources) {
            model.getData(source.link)
        }
    }

    fun currentState() {
        viewNews?.drawNewsList(newsList)
    }

    fun selectNewsTitle(title: String, source: String) {
        var selectTitle: NewsFeed? = null
        var titleForSource = newsFromAllResource.get(source)

        for (item in newsFromAllResource) {
            if (item.key.contains(source)) {
                titleForSource = item.value
            }
        }

        titleForSource?.forEach { newsFeed ->
            if(newsFeed.title == title)
                selectTitle = newsFeed
        }
        //TODO exception
        viewNews?.selectedNewsTitle(selectTitle!!)
    }

    private fun setNewsForDraw() {
        val news: MutableList<NewsFeed> = mutableListOf()
        for (item in newsFromAllResource) {
            val partNews = item.value.subList(0, 10)
            news.addAll(partNews)
        }
        news.shuffle()
        viewNews?.drawNewsList(news)
    }

    override fun successful(source: String, news: MutableList<NewsFeed>) {
        newsFromAllResource.put(source, news)
        setNewsForDraw()
    }

    override fun failed() {
        // TODO("Not yet implemented")
        Log.i("NEWS_LIST", "Not ok get data: ${model.requestData}")
    }
}