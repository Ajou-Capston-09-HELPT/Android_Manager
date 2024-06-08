package com.ajou.helptmanager.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ajou.helptmanager.home.model.*

class UserInfoViewModel : ViewModel() {

//    private val _userId = MutableLiveData<Int>()
//    val userId : LiveData<Int>
//        get() = _userId
//
//    private val _admissionId = MutableLiveData<Int?>()
//    val admissionId : LiveData<Int?>
//        get() = _admissionId
    private val _pendingUserInfo = MutableLiveData<PendingUserInfo?>()
    val pendingUserInfo : LiveData<PendingUserInfo?>
        get() = _pendingUserInfo

    private val _registeredUserInfo = MutableLiveData<RegisteredUserInfo?>()
    val registeredUserInfo : LiveData<RegisteredUserInfo?>
        get() = _registeredUserInfo

    private val _check = MutableLiveData<Boolean>()
    val check : LiveData<Boolean>
    get() = _check

    private val _chatLink = MutableLiveData<String>()
    val chatLink : LiveData<String>
    get() = _chatLink

    private val _equipment = MutableLiveData<Equipment?>()
    val equipment : LiveData<Equipment?>
    get() = _equipment

    private val _position = MutableLiveData<Int?>()
    val position : LiveData<Int?>
    get() = _position

//    fun setUserId(data: Int) {
//        _userId.postValue(data)
//    }
//
//    fun setAdmissionId(data: Int?){
//        _admissionId.postValue(data)
//    }

    fun setPendingUserInfo(data: PendingUserInfo?) {
        _pendingUserInfo.postValue(data)
    }

    fun setRegisteredUserInfo(data: RegisteredUserInfo?) {
        _registeredUserInfo.postValue(data)
    }

    fun setCheck(data: Boolean){
        _check.postValue(data)
    }

    fun setChatLink(data: String) {
        _chatLink.postValue(data)
    }

    fun setEquipmentSetting(data: List<Int>){
        val tmp = _equipment.value!!
        tmp.customWeight = data[0]
        tmp.customCount = data[1]
        tmp.customSet = data[2]
        _equipment.postValue(tmp)
    }
    fun setEquipment(data: Equipment?){
        _equipment.postValue(data)
    }

    fun setEquipment(data: Equipment, position: Int) {
        _equipment.value = data.copy()
        _position.value = position
    }

    fun setPositionNull() {
        _position.postValue(null)
    }
}