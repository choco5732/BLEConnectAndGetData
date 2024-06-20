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
        MainTabs(BleListFragment.newInstance(), R.string.main_tab_ble_list, R.drawable.ic_list_pressed),
        MainTabs(BleInfoFragment.newInstance(), R.string.main_tab_ble_data, R.drawable.ic_receive_unpressed)
    )

    fun getFragment(position: Int): Fragment {
        return fragments[position].fragment
    }

    fun getTitle(position: Int): Int {
        return fragments[position].titleRes
    }

    fun getIcon(position: Int): Int {
        return fragments[position].iconRes
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }
    override fun getItemCount(): Int {
        return fragments.size
    }

}