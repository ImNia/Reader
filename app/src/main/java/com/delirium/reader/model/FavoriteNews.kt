package com.delirium.reader.model

import io.realm.RealmObject

open class FavoriteNews(
    var guid: String? = null,
    var title: String? = null,
    var link: String? = null,
    var source: String? = null,
    var description: String? = null,
    var releaseDate: String? = null,
    var isFavorite: Boolean = false
) : RealmObject()
