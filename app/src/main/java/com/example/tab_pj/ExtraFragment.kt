package com.example.tab_pj

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.File
import android.content.Context
import androidx.lifecycle.ViewModelProvider

class ExtraFragment : Fragment () {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter_extra
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_extra, container, false)

        recyclerView = view.findViewById(R.id.recyclerview_extra)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        val names = ArrayList<String>()
        val updatedNames = updateNames(requireContext(), names)

        val viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val currentPhotosMap = viewModel.getPhotosMap().value ?: emptyMap()

        adapter = MyAdapter_extra(updatedNames, currentPhotosMap, requireContext())
        recyclerView.adapter = adapter

        viewModel.getPhotosMap().observe(viewLifecycleOwner, { photosMap ->
            adapter.updateData(photosMap)
        })

        return view
    }
    private fun updateNames(context: Context, array: ArrayList<String>): ArrayList<String> {
        // updatenamesArray 메서드의 로직
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
}


