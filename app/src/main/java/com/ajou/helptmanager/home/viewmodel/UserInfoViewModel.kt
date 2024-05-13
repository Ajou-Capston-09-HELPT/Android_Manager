package com.ajou.helptmanager.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ajou.helptmanager.home.model.UserInfo

class UserInfoViewModel : ViewModel() {

    private val _userInfo = MutableLiveData<UserInfo?>()
    val userInfo : LiveData<UserInfo?>
        get() = _userInfo

    fun setUserInfo(data: UserInfo?) {
        _userInfo.value = data
    }
}