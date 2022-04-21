package com.delirium.reader.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import com.delirium.reader.CallbackModelDB
import com.delirium.reader.model.CodeOperationModelDB
import com.delirium.reader.model.StatusCode
import com.delirium.reader.model.ModelDB
import com.delirium.reader.model.NewsFeed

class FavoritePresenter : ViewModel(), CallbackModelDB {
    private var viewFavorite: FavoriteFragment? = null
    private var modelDB = ModelDB(this)

    private var newsList: MutableList<NewsFeed> = mutableListOf()

    fun attachViewAndInit(viewFavorite: FavoriteFragment) {
        this.viewFavorite = viewFavorite
        currentState()
    }

    fun currentState() {
        getDataFromDB()
        when (newsList.isEmpty()) {
            true -> {
                viewFavorite?.drawNewsList(newsList)
                viewFavorite?.showNotNews()
            }
            false -> viewFavorite?.drawNewsList(newsList)
        }
    }

    private fun getDataFromDB() {
        newsList = modelDB.getAllFavorite() as MutableList<NewsFeed>
    }

    fun deleteNews(name: String) {
        val newsForDelete = findNewsInList(name)
        newsForDelete?.let {
            modelDB.deleteNewsInFavorite(it)
        } ?: viewFavorite?.showSnackBar(StatusCode.NEWS_NOT_FOUND)
        currentState()
    }

    fun selectNewsFromList(title: String) {
        val desiredNews = findNewsInList(title)
        desiredNews?.let {
            viewFavorite?.selectNewsForReading(it)
        } ?: viewFavorite?.showSnackBar(StatusCode.NEWS_NOT_FOUND)
    }

    private fun findNewsInList(name: String) : NewsFeed? {
        var desiredNews: NewsFeed? = null

        newsList.forEach { newsFeed ->
            if(newsFeed.title == name)
                desiredNews = newsFeed
        }

        return desiredNews
    }

    fun filterNews(source: String) {
        when(source.lowercase()) {
            //TODO in String
            "all source" -> {
                currentState()
            }
            else -> {
                val newsSet : MutableList<NewsFeed> = mutableListOf()
                for (item in newsList) {
                    if (item.source?.contains(source.lowercase()) == true) {
                        newsSet.add(item)
                    }
                }
                viewFavorite?.drawNewsList(newsSet)
            }
        }
    }

    override fun successfulModelDB(operationCode: CodeOperationModelDB, data: List<NewsFeed>) {
        when (operationCode) {
            CodeOperationModelDB.CHECK_IF_FAVORITE -> println("Contains in Favorite")
            CodeOperationModelDB.SAVE -> {
                Log.i("NEWS_LIST_PRESENTER", "Save news")
                viewFavorite?.drawNewsList(data)
            }
        }
    }

    override fun failedModelDB(statusCode: StatusCode) {
        viewFavorite?.showSnackBar(statusCode)
    }
}