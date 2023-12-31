package com.example.tab_pj

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.lifecycle.ViewModelProvider


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
        adapter.notifyDataSetChanged()

        val jsonFileName = "Num.json"
        val file = File(requireContext().filesDir, jsonFileName)
        if (file.exists()) {
            val jsonString = file.readText()
            Log.d("NumFragment", "JSON File Content: $jsonString")
        }

        // assets 폴더에서 파일을 내부 저장소로 복사
        copyAssetFileToInternalStorage(requireContext(), "Num.json")

        // ViewModel 초기화 및 데이터 로드
        val viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        loadDataIntoViewModel(viewModel, requireContext())

        val fab = view.findViewById<ExtendedFloatingActionButton>(R.id.fabNumber)
        fab.setOnClickListener {
            showDialog()
        }


        return view
    }

    //PhotoFragment 시작 시 ViewModel에 데이터 존재하도록, SharedViewModel에 초기 데이터 로드하는 메서드임
    private fun loadDataIntoViewModel(viewModel: SharedViewModel, context: Context) {
        val jsonFileName = "Num.json"
        val file = File(context.filesDir, jsonFileName)
        if (file.exists()) {
            val jsonString = file.readText()
            val listType = object : TypeToken<List<MyAdapter_num.DataItem>>() {}.type
            val dataItems = Gson().fromJson<List<MyAdapter_num.DataItem>>(jsonString, listType)
            viewModel.setNumData(dataItems)
        }
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Material_Light_Dialog_NoActionBar)
        dialog.setContentView(R.layout.popup_number_input)

        //다이얼로그 가로 너비 지정 (=전체 -32)
        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        val screenWidth = metrics.widthPixels
        val margin = 32.dpToPixels(context as Activity)  // 32dp를 픽셀로 변환
        val popupWidth = screenWidth - margin

        dialog.window?.setLayout(popupWidth, LinearLayout.LayoutParams.WRAP_CONTENT)

        val firstTextField = dialog.findViewById<EditText>(R.id.editTextName)
        val secondTextField = dialog.findViewById<EditText>(R.id.editTextNumber)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)

        saveButton.setOnClickListener {
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

            val newJsonData = createJsonData(firstText, secondText)
            addDataToJson(requireContext(), newJsonData)

            adapter.updateJsonFile(requireContext())
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
    }

    fun addDataToJson(context: Context, newData: JSONObject) {
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

    fun saveJsonToInternalStorage(context: Context, jsonData: JSONArray, fileName: String) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
            outputStream.write(jsonData.toString().toByteArray())
        }

        // 데이터 저장 후 RecyclerView 업데이트
        updateRecyclerView(context, fileName)
    }

    fun updateRecyclerView(context: Context, jsonFileName: String) {
        val jsonString = File(context.filesDir, jsonFileName).readText()
        val listType = object : TypeToken<List<MyAdapter_num.DataItem>>() {}.type
        val newDataList = Gson().fromJson<List<MyAdapter_num.DataItem>>(jsonString, listType).toMutableList()

        adapter.dataList = newDataList
        adapter.notifyDataSetChanged()

        // SharedViewModel에 데이터 저장
        val viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.setNumData(newDataList)
    }
}
    fun copyAssetFileToInternalStorage(context: Context, filename: String) {
        val file = File(context.filesDir, filename)
        if (!file.exists()) {
            context.assets.open(filename).use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

    fun createJsonData(title: String, detail: String): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("title", title)
        jsonObject.put("detail", detail)
        return jsonObject
    }

    fun Int.dpToPixels(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // 정규표현식을 사용하여 전화번호 형식을 검사
        val pattern = Regex("^\\d{3}-\\d{4}-\\d{4}$")
        return pattern.matches(phoneNumber)
        return true
    }