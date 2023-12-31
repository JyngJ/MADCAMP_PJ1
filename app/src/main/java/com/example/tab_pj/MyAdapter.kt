package com.example.tab_pj

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(
    private val items: MutableList<PhotoItem>,
    private val onDeleteClicked: (PhotoItem) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
        val deleteBtn: Button = itemView.findViewById(R.id.deleteBtn)
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

        holder.deleteBtn.setOnClickListener {
            // 아이템을 삭제하고 콜백을 호출합니다
            items.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            onDeleteClicked(currentItem)
        }

        if (currentItem.imageUriString != null) {
            Glide.with(holder.itemView)
                .load(currentItem.imageUriString)
                .into(holder.itemImage)
        } else {
            holder.itemImage.setImageResource(currentItem.imageResourceId)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}