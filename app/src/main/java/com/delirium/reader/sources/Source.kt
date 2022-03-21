package com.delirium.reader.sources

import java.io.Serializable

data class Source(
    val name: String,
    val link: String
) : Serializable
