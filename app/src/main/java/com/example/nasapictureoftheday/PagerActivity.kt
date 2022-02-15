package com.example.nasapictureoftheday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nasapictureoftheday.model.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_pager.*

class PagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)

        view_pager.adapter = ViewPagerAdapter(supportFragmentManager)
    }
}