package com.example.tab_pj

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context

class MyAdapter_num : RecyclerView.Adapter<MyAdapter_num.MyViewHolder>() {

//    var titles = arrayOf("John Doe", "2", "정산디", "4", "5", "6", "7", "8", "9", "10" )
//    var details = arrayOf("01-234-5678", "2", "010-7732-7252", "4", "5", "6", "7", "8", "9", "10" )
    var dataList = mutableListOf<DataItem>()
    data class DataItem(val title: String, val detail: String)

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.num_layout, viewGroup, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemTitle.text = dataList[position].title
        holder.itemDetail.text = dataList[position].detail
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setDataFromJson(context: Context, jsonFileName: String) {
        val jsonString: String = context.assets.open(jsonFileName)
            .bufferedReader()
            .use { it.readText() }

        val gson = Gson()
        val listType = object : TypeToken<List<DataItem>>() {}.type
        dataList = gson.fromJson(jsonString, listType)

        notifyDataSetChanged()
    }


}
