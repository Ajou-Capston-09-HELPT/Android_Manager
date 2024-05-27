package com.ajou.helptmanager.notice

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
    private val dataStore = UserDataStore()
    private val noticeService = RetrofitInstance.getInstance().create(NoticeService::class.java)

    private val _noticeList = MutableLiveData<List<NoticeResponse>>()
    val noticeList: LiveData<List<NoticeResponse>> = _noticeList

    fun getNoticeList(accessToken: String, gymId: Int) {
        viewModelScope.launch {
            val noticeListDeferred = async { noticeService.getNoticeList(accessToken, gymId) }
            val noticeListResponse = noticeListDeferred.await()
            if (noticeListResponse.isSuccessful) {
                _noticeList.value = noticeListResponse.body()?.data ?: emptyList()
            }
        }
    }

    fun uploadNotice(accessToken: String, noticeRequest: NoticeRequest) {
        viewModelScope.launch {
            val noticeListDeferred = async { noticeService.uploadNotice(accessToken, noticeRequest) }
            val noticeListResponse = noticeListDeferred.await()
            if (noticeListResponse.isSuccessful) {
                getNoticeList(accessToken, noticeRequest.gymId)
            }
        }
    }

    fun modifyNotice(accessToken: String, noticeId: Int, noticeRequest: NoticeRequest) {
        viewModelScope.launch {
            val noticeListDeferred = async { noticeService.modifyNotice(accessToken, noticeId, noticeRequest) }
            val noticeListResponse = noticeListDeferred.await()
            if (noticeListResponse.isSuccessful) {
                getNoticeList(accessToken, noticeRequest.gymId)
            }
        }
    }

    fun deleteNotice(accessToken: String, noticeId: Int, gymId: Int) {
        viewModelScope.launch {
            val noticeListDeferred = async { noticeService.deleteNotice(accessToken, noticeId) }
            val noticeListResponse = noticeListDeferred.await()
            if (noticeListResponse.isSuccessful) {
                getNoticeList(accessToken, gymId)
            }
        }
    }

}