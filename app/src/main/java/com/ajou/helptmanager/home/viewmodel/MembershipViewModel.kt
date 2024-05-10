package com.ajou.helptmanager.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ajou.helptmanager.home.model.Membership

class MembershipViewModel : ViewModel(){
    private val _membershipList = MutableLiveData<List<Membership>>()
    val membershipList: LiveData<List<Membership>> = _membershipList

    init {  // 초기 데이터 설정 (API 연동전 임시)
        _membershipList.value = listOf(
            Membership(0,"1개월 회원권", "50,000원", "50,000원"),
            Membership(1,"2개월 회원권", "100,000원", "50,000원"),
            Membership(2,"1개월 회원권", "50,000원", "50,000원"),
            Membership(3,"5개월 회원권", "50,000원", "50,000원")
        )
    }

    fun addMembership(membership: Membership) {
        val currentList = _membershipList.value?.toMutableList() ?: mutableListOf()
        currentList.add(membership)
        _membershipList.value = currentList
    }

    fun updateMembership(id: Int, updatedMembership: Membership) {
        val currentList = _membershipList.value?.toMutableList() ?: return
        if (id in currentList.indices) {
            currentList[id] = updatedMembership
            _membershipList.value = currentList
        }
    }

    fun deleteMembership(id: Int) {
        val currentList = _membershipList.value?.toMutableList() ?: return
        val indexToRemove = currentList.indexOfFirst { it.id == id }
        if (indexToRemove != -1) {
            currentList.removeAt(indexToRemove)
            _membershipList.value = currentList
        }
    }

}