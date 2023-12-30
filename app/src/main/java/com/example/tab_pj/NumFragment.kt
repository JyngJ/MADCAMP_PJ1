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
        val width = metrics.widthPixels - (8 * 4)  // 폰 가로 길이에서 마진 제외
        val popupWidth = (width / 4) * 3 + 16.dpToPixels(requireContext()) // 팝업 너비 계산

        dialog.window?.setLayout(popupWidth, LinearLayout.LayoutParams.WRAP_CONTENT)

        val firstTextField = dialog.findViewById<EditText>(R.id.namefield)
        val secondTextField = dialog.findViewById<EditText>(R.id.numberfield)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)

        saveButton.setOnClickListener {
            // JSON 저장 로직 (현재 주석 처리)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // dp를 픽셀로 변환하는 확장 함수
    private fun Int.dpToPixels(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    // ... createJsonData와 saveJsonToFile 함수 ...
}
