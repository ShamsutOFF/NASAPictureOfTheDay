package com.example.nasapictureoftheday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nasapictureoftheday.ui.main.MainFragment
import com.example.nasapictureoftheday.ui.main.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }
}