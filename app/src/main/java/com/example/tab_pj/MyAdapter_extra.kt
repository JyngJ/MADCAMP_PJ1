package com.example.tab_pj

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class MyAdapter_extra(val names: ArrayList<String>, val context: Context) : RecyclerView.Adapter<MyAdapter_extra.MyViewHolder>() {


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
        var viewPager: ViewPager2 = itemView.findViewById(R.id.viewPager)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyAdapter_extra.MyViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tab3_card_layout, viewGroup, false)
        return MyAdapter_extra.MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position < names.size) {
//            holder.itemImage.setImageResource(images[0]) // 사진 업데이트
//            holder.itemTitle.text = names[position] // 제목 업데이트
            val viewPager = holder.itemView.findViewById<ViewPager2>(R.id.viewPager)
//            val adapter = ImagePagerAdapter(images)
//            viewPager.adapter = adapter
            val adapter = ImagePagerAdapter(images.toList())
            holder.viewPager.adapter = adapter

            names[position].also { holder.itemTitle.text = it }
        }
        val jsonFileName = "Num.json"
        val file = File(holder.itemView.context.filesDir, jsonFileName)

        if (file.exists()) {
            val jsonString = file.readText()
            val jsonArray = JSONArray(jsonString)

            // 해당 위치의 JSON 객체 가져오기
            if (position >= 0 && position < jsonArray.length())  {
                val jsonObject = jsonArray.getJSONObject(position)

                // "memo" 필드의 값을 가져오고, 없으면 "메모 없음"을 사용
                val memo = jsonObject.optString("memo", "메모 없음")
                holder.itemDetail.text = memo
            } else {
                holder.itemDetail.text = "메모 없음"
            }
        } else {
            holder.itemDetail.text = "메모 없음"
        }
    }

    inner class ImagePagerAdapter(private val images: List<Int>) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.image_view_pager_item, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val imageResId = images[position]
            holder.imageView.setImageResource(imageResId)
        }

        override fun getItemCount(): Int {
            return images.size
        }

        inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.imageView)
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }
}