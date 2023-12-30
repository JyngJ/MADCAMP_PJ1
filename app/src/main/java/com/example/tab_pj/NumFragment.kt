package com.example.tab_pj

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject



class NumFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_num, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_num)
        val layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = layoutManager
        val adapter = MyAdapter_num()
        adapter.setDataFromJson(requireContext(), "Num.json")
        recyclerView.adapter = adapter

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            showDialog()
        }

        return view
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_number_input)

        val firstTextField = dialog.findViewById<EditText>(R.id.namefield)
        val secondTextField = dialog.findViewById<EditText>(R.id.numberfield)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val firstText = firstTextField.text.toString()
            val secondText = secondTextField.text.toString()
//
//            val jsonData = createJsonData(firstText, secondText)
//            saveJsonToFile(jsonData, "myData.json")

            dialog.dismiss() // 다이얼로그 닫기
        }

        dialog.show()
    }

    // ... createJsonData와 saveJsonToFile 함수 ...
}
