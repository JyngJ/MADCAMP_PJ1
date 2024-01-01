package com.example.tab_pj

import android.net.Uri

data class PhotoItem(
    val imageUri: Uri?,
    val title: String,
    val saveTime: String,
    val imageResourceId: Int
)
