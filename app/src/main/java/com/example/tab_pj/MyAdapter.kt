package com.example.tab_pj

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(private val items: List<PhotoItem>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

//    var titles = arrayOf("고영희", "강아디", "3", "4", "5")
//    var details = arrayOf("2023-12-29 20:01", "2023-12-30 18:45", "3", "4", "5")
//
//    var images = intArrayOf(
//        R.drawable.image1,
//        R.drawable.image2,
//        R.drawable.ic_launcher_foreground,
//        R.drawable.ic_launcher_foreground,
//        R.drawable.ic_launcher_foreground
//    )

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_layout, viewGroup, false)

        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = items[position]
        holder.itemTitle.text = currentItem.title
        holder.itemDetail.text = currentItem.saveTime
    if (currentItem.imageUriString != null) {
        // 이미지 URI가 있는 경우 Glide를 사용하여 이미지 로드
        Glide.with(holder.itemView)
            .load(currentItem.imageUriString)
            .into(holder.itemImage)
    } else {
        // 이미지 URI가 없는 경우 이미지 리소스 ID 사용
        holder.itemImage.setImageResource(currentItem.imageResourceId)
    }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
