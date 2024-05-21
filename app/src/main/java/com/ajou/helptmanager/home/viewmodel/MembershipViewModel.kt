    package com.ajou.helptmanager.home.viewmodel

    import ProductService
    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.map
    import androidx.lifecycle.viewModelScope
    import com.ajou.helptmanager.UserDataStore
    import com.ajou.helptmanager.home.model.Membership
    import com.ajou.helptmanager.home.model.ProductRequest
    import com.ajou.helptmanager.home.model.ProductResponseData
    import com.ajou.helptmanager.network.RetrofitInstance
    import kotlinx.coroutines.async
    import kotlinx.coroutines.launch
    import java.text.NumberFormat

    class MembershipViewModel : ViewModel(){

        private val dataStore = UserDataStore()
        private val productService = RetrofitInstance.getInstance().create(ProductService::class.java)
        private val _productList = MutableLiveData<List<ProductResponseData>>()
        val productList: LiveData<List<ProductResponseData>> = _productList
        private val _membershipList = MutableLiveData<List<Membership>>()
        val membershipList: LiveData<List<Membership>> = productList.map { productResponses ->
            productResponses.map { convertProductResponseToMembership(it) }
        }
        private val _addProductResponseData = MutableLiveData<ProductResponseData>()
        val addProductResponseData: LiveData<ProductResponseData> = _addProductResponseData
        private val _modifyProductResponseData = MutableLiveData<ProductResponseData>()
        val modifyProductResponseData: LiveData<ProductResponseData> = _modifyProductResponseData
        private val _removeProductResponse = MutableLiveData<Boolean>()
        val removeProductResponse: LiveData<Boolean> = _removeProductResponse

        private val _addProductResult = MutableLiveData<Boolean>()
        val addProductResult: LiveData<Boolean> = _addProductResult

        private val _removeProductResult = MutableLiveData<Boolean>()

        fun getProductList(accessToken: String, gymId: Int?){
            viewModelScope.launch {
                dataStore.printAllValues()
                val productListDeffered = async { productService.getProductList(accessToken!!, gymId) }
                val productListResponse = productListDeffered.await()
                if (productListResponse.isSuccessful) {
                    Log.d("getProductList", productListResponse.body()?.data.toString())
                    val sortedProductList = productListResponse.body()?.data?.sortedWith(compareBy { it.months }) ?: emptyList()
                    _productList.value = sortedProductList
                }
            }
        }

        fun addProduct(accessToken: String, gymId: Int?, productRequest: ProductRequest) {
            viewModelScope.launch {
                val currentList = _productList.value?.toMutableList() ?: mutableListOf()
                val addProductDeferred = async { productService.addProduct(accessToken!!, gymId, productRequest) }
                val addProductResponse = addProductDeferred.await()
                if (addProductResponse.isSuccessful) {
                    Log.d("addProductResponse", addProductResponse.body().toString())
                    _addProductResponseData.postValue(addProductResponse.body()?.data)
                    currentList.add(addProductResponse.body()?.data!!)
                    val sortedList = currentList.sortedWith(compareBy { it.months })
                    _productList.postValue(sortedList) // Update the product list
                    _addProductResult.postValue(true)
                }
                else {
                    Log.d("addProductResponse", addProductResponse.body().toString())
                    _addProductResult.postValue(false)
                }
            }
        }

        fun modifyProduct(accessToken: String, gymId: Int?, productId: Int?, productRequest: ProductRequest) {
            viewModelScope.launch {
                val currentList = _productList.value?.toMutableList() ?: mutableListOf()
                val modifyProductDeferred = async { productService.modifyProduct(accessToken!!, gymId, productId, productRequest) }
                val modifyProductResponse = modifyProductDeferred.await()
                if (modifyProductResponse.isSuccessful) {
                    Log.d("modifyProductResponse", modifyProductResponse.body().toString())
                    _modifyProductResponseData.postValue(modifyProductResponse.body()?.data)
                    val index = currentList.indexOfFirst { it.productId.toInt() == productId }
                    if (index != -1) {
                        currentList[index] = modifyProductResponse.body()?.data!!
                        val sortedList = currentList.sortedWith(compareBy { it.months })
                        _productList.postValue(sortedList) // Update the product list
                    }
                }
                else {
                    Log.d("modifyProductResponse", modifyProductResponse.body().toString())
                }
            }
        }

        fun removeProduct(accessToken: String, gymId: Int?, productId: Int?) {
            viewModelScope.launch {
                val currentList = _productList.value?.toMutableList() ?: mutableListOf()
                val removeProductDeferred = async { productService.removeProduct(accessToken!!, gymId, productId) }
                val removeProductResponse = removeProductDeferred.await()
                if (removeProductResponse.isSuccessful) {
                    Log.d("removeProductResponse", removeProductResponse.body().toString())
                    Log.d("removeProductResponse", productId.toString())
                    _removeProductResponse.postValue(removeProductResponse.body()?.data)
                    currentList.removeIf { it.productId.toString() == productId.toString() }
                    val sortedList = currentList.sortedWith(compareBy { it.months })
                    _productList.postValue(sortedList) // Update the product list
                    _removeProductResult.postValue(true)
                } else {
                    Log.d("removeProductResponse", removeProductResponse.body().toString())
                    _removeProductResult.postValue(false)
                }
            }
        }


        private fun convertProductResponseToMembership(productResponseData: ProductResponseData): Membership {
            val id = productResponseData.productId.toInt()
            val day = "${productResponseData.months}" // 예시로 "Monday"로 설정

            val format = NumberFormat.getNumberInstance()

            val price = format.format(productResponseData.price)
            val monthPrice = format.format(productResponseData.price / productResponseData.months)


            return Membership(id, day, price, monthPrice)
        }




    }