package com.example.tab_pj

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
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
        var itemImage: ImageView = itemView.findViewById(R.id.image_main)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
        var writeButton: MaterialButton = itemView.findViewById(R.id.write_btn)
        var modifyButton: MaterialButton = itemView.findViewById(R.id.modify_btn)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyAdapter_extra.MyViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tab3_card_layout, viewGroup, false)
        return MyAdapter_extra.MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position <= names.size) {
            holder.itemImage.setImageResource(images[0]) // 사진 업데이트
            holder.itemTitle.text = names[position] // 제목 업데이트
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
                if (memo == "메모 없음") {
                    holder.writeButton.visibility = View.VISIBLE
                    holder.modifyButton.visibility = View.GONE
                } else {
                    holder.writeButton.visibility = View.GONE
                    holder.modifyButton.visibility = View.VISIBLE
                }

            } else {
                holder.itemDetail.text = "메모 없음"
            }
        } else {
            holder.itemDetail.text = "메모 없음"

            holder.writeButton.visibility = View.VISIBLE
            holder.modifyButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }
}