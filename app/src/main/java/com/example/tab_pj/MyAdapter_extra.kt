package com.example.tab_pj

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tab3_card_layout, viewGroup, false)
        return MyViewHolder(v)
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

        holder.writeButton.setOnClickListener {
            showDialog(position, "", false)
        }

        holder.modifyButton.setOnClickListener {
            val memo = getMemo(position)
            showDialog(position, memo, true)
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

        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels - (8 * 4)
        val popupWidth = (width / 4) * 3 + 16.dpToPixels(context)
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

//1 팝업 크기 좀 키우기 (세로 )
// 위에 나오는 텍스트 바꾸기

        override fun getItemCount(): Int {
        return names.size
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
}

