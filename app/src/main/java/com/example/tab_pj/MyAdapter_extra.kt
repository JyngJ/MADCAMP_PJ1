package com.example.tab_pj

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.File

class MyAdapter_extra(val titles: List<String>, private var photosMap: Map<String, List<PhotoItem>>, val context: Context) : RecyclerView.Adapter<MyAdapter_extra.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var viewPager: ViewPager2 = itemView.findViewById(R.id.viewPager)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)
        var writeButton: MaterialButton = itemView.findViewById(R.id.write_btn)
        var modifyButton: MaterialButton = itemView.findViewById(R.id.modify_btn)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tab3_card_layout, viewGroup, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val title = titles[position]
        holder.itemTitle.text = title

        loadAndUpdatePhotosMap()

        // Use the photos for this title
        val photosForTitle = photosMap[title].orEmpty()
        Log.d("MyAdapter_extra", "title: $title, $photosForTitle")
        val adapter = ImagePagerAdapter(photosForTitle, context)

        holder.viewPager.adapter = adapter

        if (position <= titles.size) {
            holder.itemTitle.text = titles[position] // 제목 업데이트
        }
        val jsonFileName = "Num.json"
        val file = File(holder.itemView.context.filesDir, jsonFileName)

        if (file.exists()) {
            val jsonString = file.readText()
            val jsonArray = JSONArray(jsonString)

            // 해당 위치의 JSON 객체 가져오기
            if (position < jsonArray.length())  {
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

        holder.writeButton.setOnClickListener {
            showDialog(position, "", false)
        }

        holder.modifyButton.setOnClickListener {
            val memo = getMemo(position)
            showDialog(position, memo, true)
        }
    }

    private fun loadAndUpdatePhotosMap() {
        val sharedPreferences = (context as Activity).getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        Log.d("PhotoFragment", "SharedPreferences: $sharedPreferences")
        val gson = Gson()
        val json = sharedPreferences.getString("photoItems", null)
        Log.d("MyAdapter_extra", "Loaded JSON: $json")


        try {
            if (json != null) {
                val type = object : TypeToken<List<PhotoItem>>() {}.type
                val loadedItems = gson.fromJson<List<PhotoItem>>(json, type)
                Log.d("MyAdapter_extra", "Deserialized photoItems: $loadedItems")

                // title을 기준으로 PhotoItem 리스트를 Map으로 그룹화
                val newPhotosMap = loadedItems.groupBy { it.title }
                updateData(newPhotosMap)
            } else {
                Log.d("MyAdapter_extra", "No photosMap found in SharedPreferences")
            }
        } catch (e: Exception) {
                Log.e("PhotoFragment", "Error loading photo items", e)
        }
    }

    private fun getMemo(position: Int): String {
        val jsonFileName = "Num.json"
        val file = File(context.filesDir, jsonFileName)
        if (file.exists()) {
            val jsonString = file.readText()
            val jsonArray = JSONArray(jsonString)
            if (position < jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(position)
                return jsonObject.optString("memo", "")
            }
        }
        return ""
    }

    private fun showDialog(position: Int, existingMemo: String, isModify: Boolean) {
        val dialog = Dialog(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar)
        dialog.setContentView(R.layout.tab3_memo_input)

        //다이얼로그 가로 너비 지정 (=전체 -32)
        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        val screenWidth = metrics.widthPixels
        val margin = 32.dpToPixels(context)  // 32dp를 픽셀로 변환
        val popupWidth = screenWidth - margin
        dialog.window?.setLayout(popupWidth, LinearLayout.LayoutParams.WRAP_CONTENT)

        val textInputLayout = dialog.findViewById<TextInputLayout>(R.id.filledTextField)
        val editText = textInputLayout.editText
        editText?.setText(existingMemo) // 기존 메모 세팅

        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)

        val titleTextView = dialog.findViewById<TextView>(R.id.input_title) // TextView의 ID를 확인하세요.
        titleTextView.text = if (isModify) context.getString(R.string.modify_memo_title) else context.getString(R.string.add_memo)

        saveButton.setOnClickListener {
            val inputText = editText?.text.toString()
            if (inputText.isNotEmpty()) {
                updateJsonFileWithMemo(position, inputText)
            }
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    fun updateData(newData: Map<String, List<PhotoItem>>) {
        photosMap = newData
        notifyDataSetChanged()
    }

//1 팝업 크기 좀 키우기 (세로 )

    inner class ImagePagerAdapter(private val photos: List<PhotoItem>, private val context: Context) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.image_view_pager_item, parent, false)
            return ImageViewHolder(view)
        }
        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

            if (photos.isNotEmpty()) {
                // 사용자가 추가한 사진이 있는 경우
                val photoItem = photos[position]
                Glide.with(context).load(photoItem.imageUriString).into(holder.imageView)
            } else {
                // 사진이 없는 경우 placeholder 이미지 로드
                Glide.with(context).load(R.drawable.placeholder_no_image).into(holder.imageView)
            }
        }

        override fun getItemCount(): Int {
            // 사진이 없으면 placeholder를 위한 1을 반환
            return if (photos.isEmpty()) 1 else photos.size
        }

        inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.imageView)
        }
    }



    private fun Int.dpToPixels(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun updateJsonFileWithMemo(position: Int, memo: String) {
        val jsonFileName = "Num.json"
        val file = File(context.filesDir, jsonFileName)

        if (file.exists()) {
            val jsonString = file.readText()
            val jsonArray = JSONArray(jsonString)

            if (position < jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(position)
                jsonObject.put("memo", memo)

                // JSON 파일 쓰기
                file.writeText(jsonArray.toString())

                // RecyclerView 업데이트
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}