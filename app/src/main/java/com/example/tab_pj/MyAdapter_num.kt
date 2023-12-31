package com.example.tab_pj

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.ImageView
import android.widget.PopupMenu
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast

class MyAdapter_num : RecyclerView.Adapter<MyAdapter_num.MyViewHolder>() {

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
            showPopupMenu(holder.cancelButton, position)
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


    private fun showPopupMenu(cancelButton: ImageView, position: Int) {
        val popupMenu = PopupMenu(cancelButton.context, cancelButton)
        popupMenu.menuInflater.inflate(R.menu.popupmenu_deleteormodify, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_delete -> {
                    // 해당 카드를 삭제하는 로직을 구현하고, 현재 아이템의 위치(position)를 전달
                    removeCard(position, cancelButton.context)
                    true
                }
                R.id.action_modify -> {
                    val currentItem = dataList[position]
                    showEditDialog(cancelButton.context, position, currentItem.title, currentItem.detail, cancelButton.height + cancelButton.paddingTop)
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

    fun updateJsonFile(context: Context) {
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

        updateRecyclerView(context)

    }

    private fun updateRecyclerView(context: Context) {
        val jsonFileName = "Num.json"
        val file = File(context.filesDir, jsonFileName)

        if (file.exists()) {
            val jsonString = file.readText()
            val gson = Gson()
            val listType = object : TypeToken<List<DataItem>>() {}.type
            dataList = gson.fromJson(jsonString, listType)

            notifyDataSetChanged()
        }
    }

    private fun showEditDialog(context: Context, position: Int, currentTitle: String, currentDetail: String, yOffset: Int) {
        val dialog = Dialog(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar)
        dialog.setContentView(R.layout.popup_number_modify)

        //다이얼로그 가로 너비 지정 (=전체 -32)
        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        val screenWidth = metrics.widthPixels
        val margin = 32.dpToPixels(context)  // 32dp를 픽셀로 변환
        val popupWidth = screenWidth - margin
        dialog.window?.setLayout(popupWidth, LinearLayout.LayoutParams.WRAP_CONTENT)


        //팝업 위치 조정
        val windowParams = dialog.window?.attributes
        windowParams?.y = yOffset
        dialog.window?.attributes = windowParams

        val firstTextField = dialog.findViewById<EditText>(R.id.editTextName)
        val secondTextField = dialog.findViewById<EditText>(R.id.editTextNumber)

        firstTextField.setText(currentTitle)
        secondTextField.setText(currentDetail)

        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)

        saveButton.setOnClickListener {
            // 저장 버튼 클릭 시 로직 구현
            val firstText = firstTextField.text.toString()
            val secondText = secondTextField.text.toString()

            // 데이터 유효성 검사
            if (firstText.isEmpty()) {
                // 이름이 비어있을 때 에러 메시지를 보여줄 수 있습니다.
                firstTextField.error = "이름을 입력해 주세요"
                return@setOnClickListener // 클릭 이벤트 종료
            } else {
                firstTextField.error = null // 에러 메시지 지우기
            }

            if (secondText.isEmpty()) {
                // 전화번호 유효성 검사 실패 시 에러 메시지를 보여줄 수 있습니다.
                secondTextField.error = "전화번호를 입력해 주세요"
                return@setOnClickListener // 클릭 이벤트 종료
            } else if (!isValidPhoneNumber(secondText)) {
                secondTextField.error = "'000-0000-0000' 형식으로 입력해 주세요"
                return@setOnClickListener // 클릭 이벤트 종료
            } else {
                secondTextField.error = null // 에러 메시지 지우기
            }

            // 수정된 데이터로 dataList 업데이트
            if (position in dataList.indices) {
                dataList[position] = DataItem(firstText, secondText)
                notifyDataSetChanged() // 리사이클러뷰 갱신

                updateJsonFile(context) // JSON 파일 업데이트
                Toast.makeText(context, context.getString(R.string.modify_success), Toast.LENGTH_SHORT)
                    .apply {
                        setGravity(Gravity.CENTER, 0, -100)
                        show()
                    }
                }
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}

