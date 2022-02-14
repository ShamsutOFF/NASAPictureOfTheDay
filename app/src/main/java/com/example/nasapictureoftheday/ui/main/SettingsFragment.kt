package com.example.nasapictureoftheday.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nasapictureoftheday.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.settings_fragment.*

private const val TAG = "@@@ SettingsFragment"
private const val APP_THEME = "APP_THEME"

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG,
            "onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState")
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG,
            "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        super.onViewCreated(view, savedInstanceState)

        theme_chip_1.setOnClickListener { saveSettings(R.style.MarsTheme) }
        theme_chip_2.setOnClickListener { saveSettings(R.style.VenusTheme) }
        theme_chip_3.setOnClickListener { saveSettings(R.style.MoonTheme) }
    }

    private fun saveSettings(theme: Int) {
        Log.d(TAG, "saveSettings() called with: theme = $theme")
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putInt(APP_THEME, theme)
        editor?.apply()
        activity?.recreate()
    }
}