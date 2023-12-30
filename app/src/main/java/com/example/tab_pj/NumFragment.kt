package com.example.tab_pj

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.io.File


class NumFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter_num

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_num, container, false)

        recyclerView = view.findViewById(R.id.recyclerview_num)
        val layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = layoutManager

        adapter = MyAdapter_num()
        adapter.setDataFromJson(requireContext(), "Num.json")
        recyclerView.adapter = adapter

        val fab = view.findViewById<FloatingActionButton>(R.id.fabNumber)
        fab.setOnClickListener {
            showDialog()
        }

        return view
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_number_input)

        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels - (8 * 4)
        val popupWidth = (width / 4) * 3 + 16.dpToPixels(requireContext())

        dialog.window?.setLayout(popupWidth, LinearLayout.LayoutParams.WRAP_CONTENT)

        val firstTextField = dialog.findViewById<EditText>(R.id.namefield)
        val secondTextField = dialog.findViewById<EditText>(R.id.numberfield)
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
        adapter.setDataFromJson(context, jsonFileName)
        adapter.notifyDataSetChanged()
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

