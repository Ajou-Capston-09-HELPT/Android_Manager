package com.ajou.helptmanager.membership.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.membership.model.Membership
import com.ajou.helptmanager.membership.model.ProductRequest
import com.ajou.helptmanager.membership.model.ProductResponse
import com.ajou.helptmanager.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class MembershipViewModel : ViewModel() {

    val dataStore = UserDataStore()
    val accessToken: String = " " //엑세스 토큰 가져오기(JWT)

    private val _productList = MutableLiveData<List<ProductResponse>>()
    val productList: LiveData<List<ProductResponse>> = _productList

    private val _membershipList = MutableLiveData<List<Membership>>()
    val membershipList: LiveData<List<Membership>> = productList.map { productResponses ->
        productResponses.map { convertProductResponseToMembership(it) }
    }

    private val _addProductResponse = MutableLiveData<ProductResponse>()
    val addProductResponse: LiveData<ProductResponse> = _addProductResponse

    private val _modifyProductResponse = MutableLiveData<ProductResponse>()
    val modifyProductResponse: LiveData<ProductResponse> = _modifyProductResponse

    private val _removeProductResponse = MutableLiveData<Boolean>()
    val removeProductResponse: LiveData<Boolean> = _removeProductResponse

    init {
        Log.d("토큰", accessToken)
        getProductList(accessToken,1)
    }

    fun getProductList(accessToken: String, gymId: Long) {
        viewModelScope.launch {
            val response = RetrofitInstance.productService.getProductList(accessToken, gymId)
            Log.d("토큰", accessToken)
            Log.d("회원권 불러오기",response.body().toString())
            handleResponse(response, _productList)
        }
    }

    fun addProduct(gymId: Long, productRequest: ProductRequest) {
        viewModelScope.launch {
            val response = RetrofitInstance.productService.addProduct(gymId, productRequest)
            handleResponse(response, _addProductResponse)
        }
    }

    fun modifyProduct(gymId: Long, productId: Long, productRequest: ProductRequest) {
        viewModelScope.launch {
            val response = RetrofitInstance.productService.modifyProduct(gymId, productId, productRequest)
            handleResponse(response, _modifyProductResponse)
        }
    }

    fun removeProduct(productId: Long) {
        viewModelScope.launch {
            val response = RetrofitInstance.productService.removeProduct(productId)
            handleResponse(response, _removeProductResponse)
        }
    }

    private fun <T> handleResponse(response: Response<T>, liveData: MutableLiveData<T>) {
        if (response.isSuccessful) {
            liveData.postValue(response.body())
        } else {
            // 오류 처리 로직 추가
        }
    }

    fun convertProductResponseToMembership(productResponse: ProductResponse): Membership {
        // 각 필드를 원하는 형식으로 변환하여 새로운 Membership 객체를 생성하여 반환합니다.
        val id = productResponse.productId.toInt()
        val day = "${productResponse.months}" // 예시로 "Monday"로 설정
        val price = "${productResponse.price}" // 예시로 가격을 문자열로 변환하여 달러 표시를 추가합니다.
        val monthPrice = "${productResponse.price / productResponse.months}" // 예시로 월별 가격을 계산하여 문자열로 변환합니다.

        return Membership(id, day, price, monthPrice)
    }
}
/*
class MembershipViewModel : ViewModel(){
    private val _membershipList = MutableLiveData<List<Membership>>()
    val membershipList: LiveData<List<Membership>> = _membershipList
    init {  // 초기 데이터 설정 (API 연동전 임시)

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
*/