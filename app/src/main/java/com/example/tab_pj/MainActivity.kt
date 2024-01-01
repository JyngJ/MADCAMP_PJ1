package com.example.tab_pj

import PhotoFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout: TabLayout = findViewById(R.id.tabs)
        val NumFragment: Fragment = NumFragment()
        val PhotoFragment: Fragment = PhotoFragment()
        val ExtraFragment: Fragment = ExtraFragment()

        supportFragmentManager.beginTransaction().replace(R.id.tabContent, NumFragment).commit()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position) {
                    0 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.tabContent, NumFragment).commit()
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.tabContent, PhotoFragment).commit()
                    }
                    2 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.tabContent, ExtraFragment).commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }
}

