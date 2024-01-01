package com.example.tab_pj

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter_extra : RecyclerView.Adapter<MyAdapter_extra.MyViewHolder>() {

    var dataList = mutableListOf<DataItem>()
    data class DataItem(val title: String, val detail: String)

    var titles = arrayOf("고영희", "강아디", "3", "4", "5", "6", "7", "8", "9", "10")
    var details = arrayOf("2023-12-29 20:01", "2023-12-30 18:45", "3", "4", "5", "6", "7", "8", "9", "10")

    var images = intArrayOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground
    )

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.image_main)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyAdapter_extra.MyViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tab3_card_layout, viewGroup, false)
        return MyAdapter_extra.MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemImage.setImageResource(images[position])
        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}