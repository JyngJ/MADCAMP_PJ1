package com.example.tab_pj

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.tab_pj.PhotoItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class SharedViewModel : ViewModel() {
    private val photosMap = MutableLiveData<Map<String, List<PhotoItem>>>()

    init {
        photosMap.value = emptyMap()
    }

    fun getPhotosMap(): LiveData<Map<String, List<PhotoItem>>> {
        return photosMap
    }

    fun setPhotosForTitle(title: String, newPhotos: List<PhotoItem>) {
        val updatedMap = photosMap.value.orEmpty().toMutableMap()
        val currentPhotos = updatedMap[title].orEmpty()
        updatedMap[title] = currentPhotos + newPhotos
        photosMap.value = updatedMap
    }

    fun getPhotosForTitle(title: String): LiveData<List<PhotoItem>> {
        return Transformations.map(photosMap) { it[title].orEmpty() }
    }

    private val numData = MutableLiveData<List<MyAdapter_num.DataItem>>()

    fun setNumData(data: List<MyAdapter_num.DataItem>) {
        numData.value = data
    }

    fun getNumData(): LiveData<List<MyAdapter_num.DataItem>> {
        return numData
    }
    fun loadNumData(context: Context) {
        val jsonFileName = "Num.json"
        val file = File(context.filesDir, jsonFileName)
        if (file.exists()) {
            val jsonString = file.readText()
            val listType = object : TypeToken<List<MyAdapter_num.DataItem>>() {}.type
            val dataItems = Gson().fromJson<List<MyAdapter_num.DataItem>>(jsonString, listType)
            numData.value = dataItems
        }
    }

}
