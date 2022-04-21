package com.delirium.reader.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Source(
    val name: String,
    val link: String
) : Parcelable
