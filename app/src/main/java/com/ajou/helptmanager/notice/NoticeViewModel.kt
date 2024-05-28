package com.ajou.helptmanager.notice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.NoticeService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NoticeViewModel : ViewModel() {
    private val noticeService = RetrofitInstance.getInstance().create(NoticeService::class.java)

    private val _noticeList = MutableLiveData<List<NoticeResponse>>()
    val noticeList: LiveData<List<NoticeResponse>> = _noticeList



    fun getNoticeList(accessToken: String, gymId: Int) {
        viewModelScope.launch {
            val noticeListDeferred = async { noticeService.getNoticeList(accessToken, gymId) }
            val noticeListResponse = noticeListDeferred.await()
            if (noticeListResponse.isSuccessful) {
                Log.d("NoticeViewModel", noticeListResponse.body().toString())
                _noticeList.value = noticeListResponse.body()?.data ?: emptyList()
            }
        }
    }

    fun deleteNotice(accessToken: String, noticeId: Int, gymId: Int) {
        viewModelScope.launch {
            val noticeListDeferred = async { noticeService.deleteNotice(accessToken, noticeId) }
            val noticeListResponse = noticeListDeferred.await()
            if (noticeListResponse.isSuccessful) {
                Log.d("NoticeViewModel", noticeListResponse.body().toString())
                getNoticeList(accessToken, gymId)
            }
        }
    }

}


/*
    fun uploadNotice(accessToken: String, noticeUploadRequest: NoticeUploadRequest) {
        viewModelScope.launch {
            val noticeListDeferred = async { noticeService.uploadNotice(accessToken, noticeUploadRequest) }
            val noticeListResponse = noticeListDeferred.await()
            if (noticeListResponse.isSuccessful) {
                Log.d("NoticeViewModel", noticeListResponse.body().toString())
                getNoticeList(accessToken, noticeUploadRequest.gymId)
            }
            else {
                Log.d("NoticeViewModel", noticeListResponse.errorBody().toString())
            }
        }
    }

    fun modifyNotice(accessToken: String, noticeId: Int, noticeModifyRequest: NoticeModifyRequest) {
        viewModelScope.launch {
            val noticeListDeferred = async { noticeService.modifyNotice(accessToken, noticeId, noticeModifyRequest) }
            val noticeListResponse = noticeListDeferred.await()
            if (noticeListResponse.isSuccessful) {
                Log.d("NoticeViewModel", noticeListResponse.body().toString())
                getNoticeList(accessToken, noticeModifyRequest.gymId)
            }
            else{
                Log.d("NoticeViewModel", noticeListResponse.errorBody().toString())
            }
        }
    }
 */