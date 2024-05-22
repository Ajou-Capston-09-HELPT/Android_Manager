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
//        val intent = Intent(this, HomeActivity::class.java)
//        startActivity(intent)
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = dataStore.getAccessToken()
            val gymStatus = dataStore.getGymStatus()
            val gymId = dataStore.getGymId()
            Log.d("gymId  accessToken","$gymId  $accessToken")
            if (gymId == null && accessToken != null){
                val idDeferred = async { managerService.getGymId(accessToken) }
                val idResponse = idDeferred.await()
                if (idResponse.isSuccessful) {
                    val gymIdBody = JSONObject(idResponse.body()?.string())
                    val infoDeferred = async { gymService.getGymInfo(accessToken, gymIdBody.getJSONArray("data").getJSONObject(0).getString("gymId").toInt()) }
                    val infoResponse = infoDeferred.await()
                    if (infoResponse.isSuccessful){
                        dataStore.saveUserName(infoResponse.body()!!.data.gymName)
                    }
                    dataStore.saveGymId(gymIdBody.getJSONArray("data").getJSONObject(0).getString("gymId").toInt())
                    Log.d("infoResponse  body",gymIdBody.getJSONArray("data").getJSONObject(0).getString("gymId"))
                } else{
                    Log.d("infoResponse faill",idResponse.errorBody()?.string().toString())
                }
            }else{
                Log.d("gymId  accessToken","$gymId  $accessToken")
            }
            if (gymStatus != "Approved" && accessToken != null){
                val gymStatusDeferred = async { gymService.getGymStatus(accessToken) }
                val gymStatusResponse = gymStatusDeferred.await()
                if (gymStatusResponse.isSuccessful) {
                    val gymStatus = JSONObject(gymStatusResponse.body()?.string()).getJSONObject("data").getString("status")
                    dataStore.saveGymStatus(gymStatus)
                    Log.d("gymStatus body",gymStatus)
                }else{
                    Log.d("gymStatus faill",gymStatusResponse.errorBody()?.string().toString())
                }
            } else{
                Log.d("gymStatus accesToken",gymStatus.toString())
            }
            withContext(Dispatchers.Main){
                if (accessToken == null){
                    Log.d("Login 필요!","")
                    val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                    startActivity(intent)
                }else if(gymStatus != "Approved") {
                    Log.d("등록되지 않은 헬스장",accessToken.toString())
                    val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                    startActivity(intent)
                }else{
                    Log.d("Login!",accessToken.toString())
                    val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}