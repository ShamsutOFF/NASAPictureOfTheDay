package com.example.nasapictureoftheday

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nasapictureoftheday.ui.main.PictureOfTheDayFragment

private const val APP_THEME = "APP_THEME"
private const val TAG = "@@@ MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        readSettings()?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }

    private fun readSettings(): Int? {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref?.getInt(APP_THEME, R.style.VenusTheme)
    }
}