package com.ajou.helptmanager

import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.GymEquipment
import com.ajou.helptmanager.home.model.PendingUserInfo
import com.ajou.helptmanager.home.model.RegisteredUserInfo

interface AdapterToFragment {
    fun getSelectedItem(data: PendingUserInfo?)

    fun getSelectedItem(data: RegisteredUserInfo?)

    fun getSelectedItem(data: GymEquipment, position: Int)

    fun getSelectedItem(data: Equipment, position: Int)
}