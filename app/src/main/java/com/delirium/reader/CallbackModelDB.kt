package com.delirium.reader

import com.delirium.reader.model.NewsFeed
import com.delirium.reader.model.CodeOperationModelDB
import com.delirium.reader.model.StatusCode

interface CallbackModelDB {
    fun successfulModelDB(operationCode: CodeOperationModelDB, data: List<NewsFeed>)
    fun failedModelDB(statusCode: StatusCode)
}