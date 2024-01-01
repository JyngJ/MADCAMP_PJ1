package com.example.tab_pj

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val numData = MutableLiveData<List<MyAdapter_num.DataItem>>()

    fun setNumData(data: List<MyAdapter_num.DataItem>) {
        numData.value = data
    }

    fun getNumData(): LiveData<List<MyAdapter_num.DataItem>> {
        return numData
    }
}