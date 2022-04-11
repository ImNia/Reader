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

    private fun findNewsInList(name: String) : NewsFeed {
        var desiredNews: NewsFeed? = null

        newsList.forEach { newsFeed ->
            if(newsFeed.title == name)
                desiredNews = newsFeed
        }

//        Log.i("NEWS_LIST_PRESENTER", "Desire news: $desiredNews")
        return desiredNews ?: throw IllegalArgumentException()
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