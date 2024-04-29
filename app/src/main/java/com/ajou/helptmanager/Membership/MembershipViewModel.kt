package com.ajou.helptmanager.Membership

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class MembershipViewModel : ViewModel(){
    private val _membershipList = MutableLiveData<List<Membership>>()
    val membershipList: LiveData<List<Membership>> = _membershipList

    init {  // 초기 데이터 설정 (API 연동전 임시)
        _membershipList.value = listOf(
            Membership("1개월 회원권", "50,000원", "50,000원"),
            Membership("2개월 회원권", "100,000원", "50,000원"),
            Membership("1개월 회원권", "50,000원", "50,000원"),
            Membership("5개월 회원권", "50,000원", "50,000원")
        )
    }

    fun addMembership(membership: Membership) {
        val currentList = _membershipList.value?.toMutableList() ?: mutableListOf()
        currentList.add(membership)
        _membershipList.value = currentList
    }

}