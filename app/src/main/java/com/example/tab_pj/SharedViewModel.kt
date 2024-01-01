package com.example.tab_pj

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import java.io.File
import com.google.gson.reflect.TypeToken

class SharedViewModel : ViewModel() {
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