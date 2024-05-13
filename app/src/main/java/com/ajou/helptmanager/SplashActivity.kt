package com.ajou.helptmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ajou.helptmanager.auth.view.AuthActivity
import com.ajou.helptmanager.home.view.HomeActivity
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.GymService
import com.ajou.helptmanager.network.api.ManagerService
import kotlinx.coroutines.*
import org.json.JSONObject

class SplashActivity : AppCompatActivity() {
    private val managerService = RetrofitInstance.getInstance().create(ManagerService::class.java)
    private val gymService = RetrofitInstance.getInstance().create(GymService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val dataStore = UserDataStore()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
//        CoroutineScope(Dispatchers.IO).launch {
//            val accessToken = dataStore.getAccessToken()
//            val gymStatus = dataStore.getGymStatus()
//            val gymId = dataStore.getGymId()
//            if (gymId == null && accessToken != null){
//                val infoDeferred = async { managerService.getGymInfo(accessToken) }
//                val infoResponse = infoDeferred.await()
//                if (infoResponse.isSuccessful) {
//                    val gymIdBody = JSONObject(infoResponse.body()?.string())
//                    dataStore.saveGymId(gymIdBody.getJSONArray("data").getJSONObject(0).getString("gymId").toInt())
//                    Log.d("infoResponse  body",gymIdBody.getJSONArray("data").getJSONObject(0).getString("gymId"))
//                } else{
//                    Log.d("infoResponse faill",infoResponse.errorBody()?.string().toString())
//                }
//            }
//            if (gymStatus != "Registered" && accessToken != null){
//                val gymStatusDeferred = async { gymService.getGymStatus(accessToken) }
//                val gymStatusResponse = gymStatusDeferred.await()
//                if (gymStatusResponse.isSuccessful) {
//                    val gymStatus = JSONObject(gymStatusResponse.body()?.string()).getJSONObject("data").getString("status")
//                    dataStore.saveGymStatus(gymStatus)
//                    Log.d("gymStatus body",gymStatus)
//                }else{
//                    Log.d("gymStatus faill",gymStatusResponse.errorBody()?.string().toString())
//                }
//            }
//            withContext(Dispatchers.Main){
//                if (accessToken == null){
//                    Log.d("Login 필요!","")
//                    val intent = Intent(this@SplashActivity, AuthActivity::class.java)
//                    startActivity(intent)
//                }else if(gymStatus != "Registered") {
//                    Log.d("등록되지 않은 헬스장",accessToken.toString())
//                    val intent = Intent(this@SplashActivity, AuthActivity::class.java)
//                    startActivity(intent)
//                }else{
//                    Log.d("Login!",accessToken.toString())
//                    val intent = Intent(this@SplashActivity, HomeActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//        }
    }
}