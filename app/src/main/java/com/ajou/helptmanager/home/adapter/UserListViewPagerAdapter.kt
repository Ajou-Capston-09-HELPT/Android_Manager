package com.ajou.helptmanager.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ajou.helptmanager.home.view.fragment.PendingUserFragment
import com.ajou.helptmanager.home.view.fragment.RegisteredUserFragment

class UserListViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf<Fragment>(
        RegisteredUserFragment(),
        PendingUserFragment()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}