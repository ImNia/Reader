package com.delirium.reader.model

data class NewsFeed(
    var guid: String? = null,
    var title: String? = null,
    var link: String? = null,
    var source: String? = null,
    var description: String? = null,
    var releaseDate: String? = null,
    var isFavorite: Boolean = false
)