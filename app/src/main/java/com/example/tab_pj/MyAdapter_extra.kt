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

class MyAdapter_extra(val titles: List<String>, private var photosMap: Map<String, List<PhotoItem>>, val context: Context) : RecyclerView.Adapter<MyAdapter_extra.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var viewPager: ViewPager2 = itemView.findViewById(R.id.viewPager)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.tab3_card_layout, viewGroup, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val title = titles[position]
        holder.itemTitle.text = title

        // Use the photos for this title
        val photosForTitle = photosMap[title].orEmpty()
        val adapter = ImagePagerAdapter(photosForTitle, context)
        holder.viewPager.adapter = adapter
    }

    fun updateData(newData: Map<String, List<PhotoItem>>) {
        photosMap = newData
        notifyDataSetChanged()
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
