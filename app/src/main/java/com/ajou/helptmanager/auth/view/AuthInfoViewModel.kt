package com.ajou.helptmanager.auth.view

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthInfoViewModel : ViewModel() {
    private val _division = MutableLiveData<String>()
    val division : LiveData<String>
        get() = _division

    private val _bizNum = MutableLiveData<Int>()
    val bizNum : LiveData<Int>
        get() = _bizNum

    private val _bizName = MutableLiveData<String>()
    val bizName : LiveData<String>
        get() = _bizName

    private val _bizImg = MutableLiveData<Uri>()
    val bizImg : LiveData<Uri>
        get() = _bizImg

    private val _done = MutableLiveData<Boolean>(false)
    val done : LiveData<Boolean>
        get() = _done

    fun setDivisionInfo(data: String?) {
        _division.postValue(data)
    }

    fun setBizNumInfo(data: Int?) {
        _bizNum.postValue(data)
    }

    fun setBizNameInfo(data: String?) {
        _bizName.postValue(data)
    }

    fun setBizImg(data: Uri?) {
        _bizImg.postValue(data)
    }

    fun setDone(data: Boolean){
        _done.postValue(data)
    }
}