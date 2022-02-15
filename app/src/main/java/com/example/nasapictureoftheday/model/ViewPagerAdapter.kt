package com.example.nasapictureoftheday.model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.nasapictureoftheday.ui.main.FirstFragment
import com.example.nasapictureoftheday.ui.main.SecondFragment
import com.example.nasapictureoftheday.ui.main.ThirdFragment

private const val FIRST_FRAGMENT = 0
private const val SECOND_FRAGMENT = 1
private const val THIRD_FRAGMENT = 2

class ViewPagerAdapter(private val fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = arrayOf(FirstFragment(), SecondFragment(),
        ThirdFragment())

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> fragments[FIRST_FRAGMENT]
            1 -> fragments[SECOND_FRAGMENT]
            2 -> fragments[THIRD_FRAGMENT]
            else -> fragments[FIRST_FRAGMENT]
        }
    }
}