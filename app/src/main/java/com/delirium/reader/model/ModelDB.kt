package com.delirium.reader.model

import com.delirium.reader.CallbackModelDB
import com.delirium.reader.RealmConfiguration
import io.realm.Realm

class ModelDB(private val callback: CallbackModelDB) {
    private val configDB: RealmConfiguration = RealmConfiguration()

    private val realmDB: Realm = Realm.getInstance(configDB.getConfigDB())

    fun checkIsFavorite(allNews: List<NewsFeed>) : List<NewsFeed> {
        val dataFromDB = (
                realmDB.where(FavoriteNews::class.java).findAll() as MutableList<FavoriteNews>
            ).map { converterFavoriteNewsToNews(it) }

        allNews.forEach {
            it.isFavorite = dataFromDB.find { film ->
                film.guid == it.guid
            } != null
        }
//        callback.successfulModelDB(CodeOperationModelDB.CHECK_IF_FAVORITE, allNews)
        return allNews
    }

    fun saveNewsToFavorite(news: NewsFeed) {
        realmDB.beginTransaction()
        realmDB.copyToRealm(converterNewsToFavoriteNews(news))
        realmDB.commitTransaction()
        callback.successfulModelDB(CodeOperationModelDB.SAVE, mutableListOf())
    }

    fun deleteNewsInFavorite(news: NewsFeed) {
        realmDB.beginTransaction()
        val removeObject = realmDB.where(FavoriteNews::class.java)
            .equalTo("guid", news.guid)
            .findFirst()
        removeObject?.deleteFromRealm()
        realmDB.commitTransaction()
    }

    fun deleteNewsInFavoriteList(news: NewsFeed): List<NewsFeed> {
        deleteNewsInFavorite(news)
        return getAllFavorite()
    }

    fun getAllFavorite(): List<NewsFeed> {
        val data = realmDB.where(FavoriteNews::class.java).findAll()

        return data.map { converterFavoriteNewsToNews(it) }
    }

    private fun converterNewsToFavoriteNews(news: NewsFeed) : FavoriteNews {
        return FavoriteNews(
            guid = news.guid,
            title = news.title,
            link = news.link,
            source = news.source,
            description = news.description,
            releaseDate = news.releaseDate,
            isFavorite = news.isFavorite
        )
    }
    private fun converterFavoriteNewsToNews(favoriteNews: FavoriteNews) : NewsFeed {
        return NewsFeed(
            guid = favoriteNews.guid,
            title = favoriteNews.title,
            link = favoriteNews.link,
            source = favoriteNews.source,
            description = favoriteNews.description,
            releaseDate = favoriteNews.releaseDate,
            isFavorite = favoriteNews.isFavorite
        )
    }
}