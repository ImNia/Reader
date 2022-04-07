package com.delirium.reader

import io.realm.RealmConfiguration

class RealmConfiguration {
    private val config: RealmConfiguration =
        RealmConfiguration.Builder()
            .name("readerNews.realm")
            .schemaVersion(1)
            .build()

    fun getConfigDB() = config
}