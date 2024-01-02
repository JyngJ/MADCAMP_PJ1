package com.example.tab_pj

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class MyAdapter_extra(val titles: List<String>, val photosMap: LiveData<Map<String, List<PhotoItem>>>, val context: Context) : RecyclerView.Adapter<MyAdapter_extra.MyViewHolder>() {


//    var images = intArrayOf(
//        R.drawable.image1,
//        R.drawable.image2,
//        R.drawable.ic_launcher_foreground,
//        R.drawable.ic_launcher_foreground,
//        R.drawable.ic_launcher_foreground,
//        R.drawable.ic_launcher_foreground,
//        R.drawable.ic_launcher_foreground,
//        R.drawable.ic_launcher_foreground,
//        R.drawable.ic_launcher_foreground,
//        R.drawable.ic_launcher_foreground
//    )

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var viewPager: ViewPager2 = itemView.findViewById(R.id.viewPager)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.tab3_card_layout, viewGroup, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val title = titles[position]
        holder.itemTitle.text = title

        // Set up the ViewPager for each title
        val photosForTitle = photosMap.value?.get(title).orEmpty()
        val adapter = ImagePagerAdapter(photosForTitle, context)
        holder.viewPager.adapter = adapter

        // JSON processing for itemDetail (ensure the JSON structure matches your implementation)
        val jsonFileName = "Num.json"
        val file = File(holder.itemView.context.filesDir, jsonFileName)
        if (file.exists()) {
            val jsonString = file.readText()
            val jsonArray = JSONArray(jsonString)
            if (position >= 0 && position < jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(position)
                val memo = jsonObject.optString("memo", "메모 없음")
                holder.itemDetail.text = memo
            } else {
                holder.itemDetail.text = "메모 없음"
            }
        } else {
            holder.itemDetail.text = "메모 없음"
        }
    }

    inner class ImagePagerAdapter(private val photos: List<PhotoItem>, private val context: Context) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.image_view_pager_item, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val photoItem = photos[position]
            Glide.with(context).load(photoItem.imageUri).into(holder.imageView)
        }

        override fun getItemCount(): Int {
            return photos.size
        }

        inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.imageView)
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}