package com.delirium.reader.sources

import android.util.Log
import androidx.lifecycle.ViewModel

class SourcePresenter : ViewModel() {
    private var viewSource: SourceList? = null

    //TODO Must be stored in other place
    private var sourceList : List<Source> = listOf(
        Source("Lenta", "https://lenta.ru/rss/news/"),
//        Source("Meduza", "https://meduza.io/rss/all/"),
        Source("Habr", "https://habr.com/ru/rss/"),
        Source("Phoronix", "https://www.phoronix.com/rss.php")
    )

    fun attachView(viewSource: SourceList) {
        this.viewSource = viewSource
    }

    fun currentState() {
        viewSource?.drawSourceList(sourceList)
    }

    fun selectSource(nameSource: String) {
        Log.i("SOURCE_PRESENTER", nameSource)
        var sourceSelect: Source? = null
        sourceList.forEach { source ->
            if (source.name == nameSource) {
                sourceSelect = source
            }
        }

        //TODO exception
        viewSource?.selectedSource(sourceSelect!!)
    }
}