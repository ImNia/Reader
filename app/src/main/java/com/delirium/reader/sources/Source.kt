package com.delirium.reader.sources

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Source(
    val name: String,
    val link: String
) : Parcelable
