package com.example.protocol20datainfo.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.protocol20datainfo.R
import com.example.protocol20datainfo.data.MainTabs
import com.example.protocol20datainfo.presentation.BleInfoFragment
import com.example.protocol20datainfo.presentation.BleListFragment

class MainViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(
    fragmentActivity
) {

    private val fragments = listOf(
        MainTabs(BleListFragment.newInstance(), R.string.main_tab_ble_list),
        MainTabs(BleInfoFragment.newInstance(), R.string.main_tab_ble_detail)
    )

    fun getFragment(position: Int): Fragment {
        return fragments[position].fragment
    }

    fun getTitle(position: Int): Int {
        return fragments[position].titleRes
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }
    override fun getItemCount(): Int {
        return fragments.size
    }

}