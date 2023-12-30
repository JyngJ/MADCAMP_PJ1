package com.example.tab_pj

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import android.widget.ImageView
import android.widget.PopupMenu
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import android.view.MenuInflater


class MyAdapter_num : RecyclerView.Adapter<MyAdapter_num.MyViewHolder>() {

//    var titles = arrayOf("John Doe", "2", "정산디", "4", "5", "6", "7", "8", "9", "10" )
//    var details = arrayOf("01-234-5678", "2", "010-7732-7252", "4", "5", "6", "7", "8", "9", "10" )
    var dataList = mutableListOf<DataItem>()
    data class DataItem(val title: String, val detail: String)

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
        var cancelButton: ImageView = itemView.findViewById(R.id.cancel_button)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.num_layout, viewGroup, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemTitle.text = dataList[position].title
        holder.itemDetail.text = dataList[position].detail
        holder.cancelButton.setOnClickListener {
            // 해당 카드를 삭제하는 로직을 구현
            showPopupMenu(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setDataFromJson(context: Context, jsonFileName: String) {
        val file = File(context.filesDir, jsonFileName)
        if (file.exists()) {
            val jsonString = file.readText()
            val gson = Gson()
            val listType = object : TypeToken<List<DataItem>>() {}.type
            dataList = gson.fromJson(jsonString, listType)

            notifyDataSetChanged()
        }
    }


    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.popupmenu_deleteormodify, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_delete -> {
                    // 해당 카드를 삭제하는 로직을 구현하고, 현재 아이템의 위치(position)를 전달
                    removeCard(position, view.context)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    fun removeCard(position: Int, context: Context) {
        if (position < 0 || position >= dataList.size) return

        // 카드를 리스트에서 삭제
        dataList.removeAt(position)

        // JSON 파일 업데이트
        updateJsonFile(context)

        // 리사이클러뷰 갱신
        notifyDataSetChanged()
    }

    private fun updateJsonFile(context: Context) {
        val jsonFileName = "Num.json"
        val file = File(context.filesDir, jsonFileName)

        if (!file.exists()) {
            // JSON 파일이 존재하지 않으면 아무 작업도 수행하지 않음
            return
        }

        // JSON 배열 생성
        val jsonArray = JSONArray()
        for (dataItem in dataList) {
            val jsonObject = JSONObject()
            jsonObject.put("title", dataItem.title)
            jsonObject.put("detail", dataItem.detail)
            jsonArray.put(jsonObject)
        }

        // JSON 파일 업데이트
        context.openFileOutput(jsonFileName, Context.MODE_PRIVATE).use { outputStream ->
            outputStream.write(jsonArray.toString().toByteArray())
        }
    }

}
