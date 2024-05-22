package com.ajou.helptmanager.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ajou.helptmanager.home.model.RegisteredUserInfo

class UserInfoViewModel : ViewModel() {

    private val _userId = MutableLiveData<Int>()
    val userId : LiveData<Int>
        get() = _userId

    private val _admissionId = MutableLiveData<Int>()
    val admissionId : LiveData<Int>
        get() = _admissionId

    private val _check = MutableLiveData<Boolean>()
    val check : LiveData<Boolean>
    get() = _check

    fun setUserId(data: Int) {
        _userId.postValue(data)
    }

    fun setAdmissionId(data: Int){
        _admissionId.postValue(data)
    }

    fun setCheck(data: Boolean){
        _check.postValue(data)
    }
}