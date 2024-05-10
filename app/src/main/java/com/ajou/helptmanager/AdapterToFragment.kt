package com.ajou.helptmanager

import com.ajou.helptmanager.home.model.UserInfo

interface AdapterToFragment {
    fun getSelectedItem(data : UserInfo)
}