package com.example.tab_pj

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExtraFragment : Fragment () {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter_num
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_extra, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_extra)
        val layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = layoutManager
        val adapter = MyAdapter_extra()
        recyclerView.adapter = adapter


        return view
    }
}
//    class PhotoFragment : Fragment() {
//        override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//        ): View? {
//            val view = inflater.inflate(R.layout.fragment_photo, container, false)
//
//            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_main)
//            val layoutManager = GridLayoutManager(requireContext(), 2)
//            recyclerView.layoutManager = layoutManager
//            val adapter = MyAdapter()
//            recyclerView.adapter = adapter
//
//
//            return view
//        }
//    }
//}
