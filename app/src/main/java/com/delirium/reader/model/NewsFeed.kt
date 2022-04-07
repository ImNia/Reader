package com.delirium.reader.model

data class NewsFeed(
    var title: String? = null,
    var link: String? = null,
    var source: String? = null,
    var description: String? = null,
    var releaseDate: String? = null
)