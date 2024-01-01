package com.example.tab_pj

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class MyAdapter_extra : RecyclerView.Adapter<MyAdapter_extra.MyViewHolder>() {

    var dataList = mutableListOf<DataItem>()
    data class DataItem(val title: String, val detail: String)

    var titles = arrayOf("고영희", "강아디", "3", "4", "5", "6", "7", "8", "9", "10")

    var details = arrayOf(" Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris tincidunt vitae mauris sed lacinia. Nullam varius est ac mauris placerat, sed sollicitudin nulla gravida. Vestibulum faucibus odio ac lorem lobortis sagittis. Cras sagittis tristique finibus. Pellentesque diam odio, egestas eu scelerisque ut, cursus at sem.  ", "2023-12-30 18:45", "3", "4", "5", "6", "7", "8", "9", "10")

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

    var names = ArrayList<String>()     // 이름들의 어레이리스트 생성
    // 주소록을 수정했을 때 업데이트 되어야 함
    // 업데이트가 되면 여기서 보여질 때도 업데이트 되어야 함

    fun updatenamesArray(context: Context, array: ArrayList<String>): ArrayList<String> {
        val jsonFileName = "Num.json"
        val file = File(context.filesDir, jsonFileName)

        if (file.exists()) {
            val jsonString = file.readText()
            val jsonArray = JSONArray(jsonString)

            // 1) json 파일을 돌아서 names 배열을 임시로 만들기
            val tempNames = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val title = jsonObject.optString("title", "")
                if (title.isNotEmpty()) {
                    tempNames.add(title)
                }
            }

            // 2) 입력받은 어레이에 names 배열의 요소들이 있는지 확인하고 업데이트
            for (name in tempNames) {
                if (!array.contains(name)) {
                    // 2-2) 없다면 배열 맨 뒤에 추가
                    array.add(name)
                }
            }

            // 3) names 배열에 없는 요소들은 입력받은 어레이에서 지우기
            val iterator = array.iterator()
            while (iterator.hasNext()) {
                val name = iterator.next()
                if (!tempNames.contains(name)) {
                    iterator.remove()
                }
            }
        }

        return array
    }
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
        names = updatenamesArray(holder.itemView.context, names)
        holder.itemImage.setImageResource(images[0])                        // 여기 일단 다 똑같은 사진 보여주는걸로 되어있음

        if (position < names.size)
            holder.itemTitle.text = names[position]

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

    override fun getItemCount(): Int {
        return titles.size
    }
}