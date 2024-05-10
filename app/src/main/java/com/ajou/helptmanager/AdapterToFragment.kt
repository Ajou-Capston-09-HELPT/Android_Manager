package com.ajou.helptmanager

import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.UserInfo

interface AdapterToFragment {
    fun getSelectedItem(data : UserInfo)

    fun getSelectedItem(data : Equipment, position : Int)
}