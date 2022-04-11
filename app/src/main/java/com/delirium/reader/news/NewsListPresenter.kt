package com.delirium.reader.news

import android.util.Log
import androidx.lifecycle.ViewModel
import com.delirium.reader.CallbackModelDB
import com.delirium.reader.CallbackNews
import com.delirium.reader.model.Model
import com.delirium.reader.model.NewsFeed
import com.delirium.reader.model.CodeOperationModelDB
import com.delirium.reader.model.ModelDB
import com.delirium.reader.sources.Source
import java.lang.IllegalArgumentException

class NewsListPresenter : ViewModel(), CallbackNews, CallbackModelDB {
    private var viewNews: NewsList? = null
    private var model = Model(this)
    private var modelDB = ModelDB(this)

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
        val desiredNews = findNewsInList(title, source)
        viewNews?.selectedNewsTitle(desiredNews)
    }

    fun selectFavoriteNews(title: String, source: String) {
        val desiredNews = findNewsInList(title, source)
        when (desiredNews.isFavorite) {
            true -> modelDB.deleteNewsInFavorite(desiredNews)
            false -> {
                desiredNews.isFavorite = true
                modelDB.saveNewsToFavorite(desiredNews)
            }
        }
//        currentState()
    }
    
    private fun findNewsInList(title: String, source: String) : NewsFeed {
        var desiredNews: NewsFeed? = null
        var titleForSource = newsFromAllResource.get(source)

        for (item in newsFromAllResource) {
            if (item.key.contains(source)) {
                titleForSource = item.value
            }
        }

        titleForSource?.forEach { newsFeed ->
            if(newsFeed.title == title)
                desiredNews = newsFeed
        }

//        Log.i("NEWS_LIST_PRESENTER", "Desire news: $desiredNews")
        return desiredNews ?: throw IllegalArgumentException()
    }
    
    private fun setNewsForDraw() {
        val news: MutableList<NewsFeed> = mutableListOf()
        for (item in newsFromAllResource) {
            val partNews = item.value.subList(0, 10)
            news.addAll(partNews)
        }
        news.shuffle()
        newsList = news
        currentState()
    }

    override fun successfulNews(source: String, news: MutableList<NewsFeed>) {
        val updateNews = modelDB.checkIsFavorite(news)
        newsFromAllResource.put(source, updateNews)
        setNewsForDraw()
    }

    override fun failedNews() {
        // TODO("Not yet implemented")
        Log.i("NEWS_LIST", "Not ok get data: ${model.requestData}")
    }

    override fun successfulModelDB(operationCode: CodeOperationModelDB, data: List<NewsFeed>) {
        when (operationCode) {
            CodeOperationModelDB.CHECK_IF_FAVORITE -> println("Contains in Favorite")
            CodeOperationModelDB.SAVE -> {
                Log.i("NEWS_LIST_PRESENTER", "Save news")
                viewNews?.drawNewsList(newsList)
            }
        }
    }

    override fun failedModelDB() {
        Log.i("NEWS_LIST", "Not ok in ModelDB")
    }
}