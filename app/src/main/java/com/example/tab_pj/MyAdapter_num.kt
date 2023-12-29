package com.example.tab_pj

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter_num : RecyclerView.Adapter<MyAdapter_num.MyViewHolder>() {

    var titles = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10" )
    var details = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10" )

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
        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]
    }

    override fun getItemCount(): Int {
        return titles.size
    }

}
