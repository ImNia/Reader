package com.delirium.reader.news

import android.util.Log
import androidx.lifecycle.ViewModel
import com.delirium.reader.CallbackModelDB
import com.delirium.reader.CallbackNews
import com.delirium.reader.model.*
import com.delirium.reader.model.Source
import kotlin.collections.HashMap

class NewsListPresenter : ViewModel(), CallbackNews, CallbackModelDB {
    private var viewNews: NewsList? = null
    private var model = Model(this)
    private var modelDB = ModelDB(this)

    private var newsList: MutableList<NewsFeed> = mutableListOf()
    private var newsFromAllResource: HashMap<String, List<NewsFeed>> = hashMapOf()

    fun attachViewAndInit(viewNews: NewsList, sources: List<Source>) {
        this.viewNews = viewNews
        if (newsList.isEmpty()) {
            for (source in sources) {
                model.getData(source.link)
            }
        }
    }

    fun currentState() {
        viewNews?.drawNewsList(newsList)
    }

    fun selectNewsTitle(title: String, source: String) {
        val desiredNews = findNewsInList(title, source)
        desiredNews?.let { news ->
            viewNews?.selectedNewsTitle(news)
        }
    }

    fun selectFavoriteNews(title: String, source: String) {
        val desiredNews = findNewsInList(title, source)
        desiredNews?.let { news ->
            when (news.isFavorite) {
                true -> modelDB.deleteNewsInFavorite(news)
                false -> {
                    news.isFavorite = true
                    modelDB.saveNewsToFavorite(news)
                }
            }
        }
    }
    
    private fun findNewsInList(title: String, source: String) : NewsFeed? {
        var desiredNews: NewsFeed? = null
        var newsSetBySource : List<NewsFeed> = listOf()

        for (item in newsFromAllResource) {
            if (item.key.contains(source)) {
                newsSetBySource = item.value
            }
        }

        newsSetBySource.forEach { newsFeed ->
            if(newsFeed.title == title)
                desiredNews = newsFeed
        }

        return desiredNews
    }
    
    private fun setNewsForDraw() {
        val news: MutableList<NewsFeed> = mutableListOf()
        for (item in newsFromAllResource) {
            val partNews = item.value.subList(0, 10)
            news.addAll(partNews)
        }

        newsList = news.sortedWith(compareByDescending { it.releaseDate }) as MutableList<NewsFeed>
        currentState()
    }

    fun filterNews(source: String) {
        when(source.lowercase()) {
            //TODO in String
            "all source" -> {
                setNewsForDraw()
            }
            else -> {
                var newsSet : List<NewsFeed> = listOf()
                for (item in newsFromAllResource) {
                    if (item.key.contains(source.lowercase())) {
                        newsSet = item.value
                    }
                }
                newsList = newsSet as MutableList<NewsFeed>
                currentState()
            }
        }
    }

    override fun successfulNews(source: String, news: MutableList<NewsFeed>) {
        val updateNews = modelDB.checkIsFavorite(news)
        newsFromAllResource.put(source, updateNews)
        setNewsForDraw()
    }

    override fun failedNews(statusCode: StatusCode) {
        viewNews?.showSnackBar(statusCode)
    }

    override fun successfulModelDB(operationCode: CodeOperationModelDB, data: List<NewsFeed>) {
        when (operationCode) {
            CodeOperationModelDB.CHECK_IF_FAVORITE -> println("Contains in Favorite")
            CodeOperationModelDB.SAVE -> {
                viewNews?.drawNewsList(newsList)
            }
        }
    }

    override fun failedModelDB() {
        Log.i("NEWS_LIST", "Not ok in ModelDB")
    }
}