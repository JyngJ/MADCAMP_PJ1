package com.example.tab_pj

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyAdapter(private var items: List<PhotoItem>, private val context: Context) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
        var delete_btn: MaterialButton = itemView.findViewById(R.id.delete_btn)
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

        holder.delete_btn.setOnClickListener {
            val updatedItems = items.toMutableList()
            updatedItems.removeAt(position)
            deletePhotoFromJson(currentItem) // JSON에서 사진 삭제 함수 호출
            updateData(updatedItems)

            val sharedPreferences = (context as Activity).getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            Log.d("PhotoFragment", "SharedPreferences: $sharedPreferences")
            val json = sharedPreferences.getString("photoItems", null)
            val gson = Gson()
            val type = object : TypeToken<List<PhotoItem>>() {}.type
            val loadedItems = gson.fromJson<List<PhotoItem>>(json, type)
            Log.d("MyAdapter_extra", "photoItems 불러와서 확인: $loadedItems")

        }
    }

    private fun deletePhotoFromJson(photoItem: PhotoItem) {
        val sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val gson = Gson()
//
//        val json = sharedPreferences.getString("photoItems", null)
//        val type = object : TypeToken<MutableList<PhotoItem>>() {}.type
//        val photoItems = if (json != null) gson.fromJson<MutableList<PhotoItem>>(json, type) else mutableListOf()
//        Log.d("MyAdapter", "Delete Photo -> found this to delete: photoitems: , $photoItems")
//
//        Log.d("MyAdapter", "Attempting to delete: $photoItem")
//
//        val isRemoved = photoItems.removeAll { it.title == photoItem.title && it.imageUriString == photoItem.imageUriString }
//
//        // 삭제 후 리스트 상태와 삭제 성공 여부 확인
//        Log.d("MyAdapter", "After deletion, photoItems: $photoItems, isRemoved: $isRemoved")
//
//        // 새로운 리스트를 JSON으로 변환하여 저장
//        val newJson = gson.toJson(photoItems)
//        // SharedPreferences.Editor를 가져와서 편집을 시작
//        val editor = sharedPreferences.edit()
//
//        // "photoItems" 키에 대한 값을 newJson 문자열로 설정
//        editor.putString("photoItems", newJson)
//
//        // 변경사항을 비동기적으로 커밋(apply)
//        editor.commit()
//        Log.d("MyAdapter", "Updated JSON in SharedPreferences: $newJson")
//
//        // SharedPreferences에서 새로운 데이터 불러오기
//        val updatedJson = sharedPreferences.getString("photoItems", null)
//        val updatedPhotoItems = if (updatedJson != null) gson.fromJson<MutableList<PhotoItem>>(updatedJson, type) else mutableListOf()
//        Log.d("MyAdapter", "Updated JSON from SharedPreferences after deletion: $updatedPhotoItems")
        // photoItems JSON 배열에서 삭제
        val jsonPhotoItems = sharedPreferences.getString("photoItems", null)
        val typePhotoItems = object : TypeToken<MutableList<PhotoItem>>() {}.type
        val photoItemsList = if (jsonPhotoItems != null) gson.fromJson<MutableList<PhotoItem>>(jsonPhotoItems, typePhotoItems) else mutableListOf()
        val isPhotoItemRemoved = photoItemsList.removeAll { it.title == photoItem.title && it.imageUriString == photoItem.imageUriString }

        if (isPhotoItemRemoved) {
            val editor = sharedPreferences.edit()
            editor.putString("photoItems", gson.toJson(photoItemsList))
            editor.apply()
        }

        // photosMap에서 삭제
        val jsonPhotosMap = sharedPreferences.getString("photosMap", null)
        val typePhotosMap = object : TypeToken<Map<String, List<PhotoItem>>>() {}.type
        val photosMap = if (jsonPhotosMap != null) gson.fromJson<Map<String, List<PhotoItem>>>(jsonPhotosMap, typePhotosMap).toMutableMap() else mutableMapOf()

        photosMap[photoItem.title]?.let { list ->
            val updatedList = list.filterNot { it.imageUriString == photoItem.imageUriString }
            if (list.size != updatedList.size) {
                photosMap[photoItem.title] = updatedList
                val editor = sharedPreferences.edit()
                editor.putString("photosMap", gson.toJson(photosMap))
                editor.apply()
            }
        }

        // 로깅을 위한 메시지
        Log.d("MyAdapter", "Deleted photoItem from photoItems: $isPhotoItemRemoved")
        Log.d("MyAdapter", "Updated photosMap in SharedPreferences: ${gson.toJson(photosMap)}")
    }



    private fun updateData(newItems: List<PhotoItem>) {
        items = newItems
        notifyDataSetChanged() // 리스트 변경을 알림
        Log.d("MyAdapter", "Dataset Updated?: $items")
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
