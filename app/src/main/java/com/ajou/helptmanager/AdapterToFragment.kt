package com.ajou.helptmanager

import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.GymEquipment

interface AdapterToFragment {
    fun getSelectedItem(userId: Int, admissionId: Int?)

    fun getSelectedItem(data: GymEquipment, position: Int)

    fun getSelectedItem(data: Equipment, position: Int)
}