package com.delirium.reader.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import com.delirium.reader.CallbackModelDB
import com.delirium.reader.model.CodeOperationModelDB
import com.delirium.reader.model.ModelDB
import com.delirium.reader.model.NewsFeed
import java.lang.IllegalArgumentException

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
        modelDB.deleteNewsInFavorite(newsForDelete)
        currentState()
    }

    fun selectNewsFromList(title: String) {
        val desiredNews = findNewsInList(title)
        viewFavorite?.selectNewsForReading(desiredNews)
    }

    private fun findNewsInList(name: String) : NewsFeed {
        var desiredNews: NewsFeed? = null

        newsList.forEach { newsFeed ->
            if(newsFeed.title == name)
                desiredNews = newsFeed
        }

        return desiredNews ?: throw IllegalArgumentException()
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

    override fun failedModelDB() {
        //TODO("Not yet implemented")
        Log.i("FAVORITE_PRESENTER", "modelDB have a problem")
    }
}