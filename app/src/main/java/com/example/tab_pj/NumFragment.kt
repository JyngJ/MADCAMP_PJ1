package com.example.tab_pj

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class NumFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter_num

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_num, container, false)

        // recyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerview_num)
        val layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = layoutManager

        // adapter 초기화
        adapter = MyAdapter_num()
        // 내부 저장소에서 파일 읽기
        adapter.setDataFromJson(requireContext(), "Num.json")
        recyclerView.adapter = adapter

        // assets 폴더에서 파일을 내부 저장소로 복사
        copyAssetFileToInternalStorage(requireContext(), "Num.json")

        // 내부 저장소에서 파일 읽기
        adapter.setDataFromJson(requireContext(), "Num.json")

        val fab = view.findViewById<ExtendedFloatingActionButton>(R.id.fabNumber)
        fab.setOnClickListener {
            showDialog()
        }

        return view
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Material_Light_Dialog_NoActionBar)
        dialog.setContentView(R.layout.popup_number_input)

        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels - (8 * 4)
        val popupWidth = (width / 4) * 3 + 16.dpToPixels(requireContext())

        dialog.window?.setLayout(popupWidth, LinearLayout.LayoutParams.WRAP_CONTENT)

        val firstTextField = dialog.findViewById<EditText>(R.id.editTextName)
        val secondTextField = dialog.findViewById<EditText>(R.id.editTextNumber)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)

        saveButton.setOnClickListener {
            val firstText = firstTextField.text.toString()
            val secondText = secondTextField.text.toString()

            val newJsonData = createJsonData(firstText, secondText)
            addDataToJson(requireContext(), newJsonData)

            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
    }

    private fun addDataToJson(context: Context, newData: JSONObject) {
        val jsonFileName = "Num.json"
        val file = File(context.filesDir, jsonFileName)

        if (!file.exists()) {
            // assets 폴더에서 파일을 내부 저장소로 복사 (초기 실행 시)
            context.assets.open(jsonFileName).use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

        val jsonArray = JSONArray(file.readText())
        jsonArray.put(newData)

        saveJsonToInternalStorage(context, jsonArray, jsonFileName)
    }
    private fun saveJsonToInternalStorage(context: Context, jsonData: JSONArray, fileName: String) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
            outputStream.write(jsonData.toString().toByteArray())
        }

        // 데이터 저장 후 RecyclerView 업데이트
        updateRecyclerView(context, fileName)
    }

    private fun updateRecyclerView(context: Context, jsonFileName: String) {
        val jsonString = File(context.filesDir, jsonFileName).readText()
        val listType = object : TypeToken<List<MyAdapter_num.DataItem>>() {}.type
        val newDataList = Gson().fromJson<List<MyAdapter_num.DataItem>>(jsonString, listType).toMutableList()

        adapter.dataList = newDataList
        adapter.notifyDataSetChanged()
    }
}
    private fun copyAssetFileToInternalStorage(context: Context, filename: String) {
        val file = File(context.filesDir, filename)
        if (!file.exists()) {
            context.assets.open(filename).use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

    private fun createJsonData(title: String, detail: String): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("title", title)
        jsonObject.put("detail", detail)
        return jsonObject
    }

    private fun Int.dpToPixels(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

