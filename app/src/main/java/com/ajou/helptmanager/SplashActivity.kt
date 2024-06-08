package com.ajou.helptmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
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
        val logoGif = findViewById<ImageView>(R.id.logo)
        val dataStore = UserDataStore()
        val gifDrawable =
            pl.droidsonroids.gif.GifDrawable(resources, R.drawable.splash_logo)
        logoGif.setImageDrawable(gifDrawable)

            lifecycleScope.launch(Dispatchers.IO) {
                delay(2000)
                val accessToken = dataStore.getAccessToken()
                val refreshToken = dataStore.getRefreshToken()
                val gymStatus = dataStore.getGymStatus()
                val gymId = dataStore.getGymId()
                var finalAccessToken = accessToken
                var finalRefreshToken = refreshToken

                if (accessToken != null){
                    val tokenDeferred = async { managerService.getGymId(accessToken) }
                    val tokenResponse = tokenDeferred.await()
                    if (!tokenResponse.isSuccessful){
                        Log.d("tokenResponse fail",tokenResponse.errorBody()?.string().toString())
                        val newTokenDeferred = async { managerService.getNewToken(refreshToken!!) }
                        val newTokenResponse = newTokenDeferred.await()
                        if (newTokenResponse.isSuccessful){
                            val tokenBody = JSONObject(newTokenResponse.body()?.string())
                            finalAccessToken = "Bearer " + tokenBody.getJSONObject("data").getString("accessToken").toString()
                            finalRefreshToken = "Bearer " + tokenBody.getJSONObject("data").getString("refreshToken").toString()

                            dataStore.saveAccessToken(finalAccessToken)
                            dataStore.saveRefreshToken(finalRefreshToken)
                        }
                    }else{
                        Log.d("tokenResponse success?","")
                    }
                }
                if (gymId == null && finalAccessToken != null) {
                    val idDeferred = async { managerService.getGymId(finalAccessToken) }
                    val idResponse = idDeferred.await()
                    if (idResponse.isSuccessful) {
                        val gymIdBody = JSONObject(idResponse.body()?.string())
                        val infoDeferred = async {
                            gymService.getGymInfo(
                                finalAccessToken,
                                gymIdBody.getJSONObject("data").getString("gymId").toInt()
                            )
                        }
                        val infoResponse = infoDeferred.await()
                        if (infoResponse.isSuccessful) {
                            dataStore.saveUserName(infoResponse.body()!!.data.gymName)
                        }
                        dataStore.saveGymId(
                            gymIdBody.getJSONObject("data").getString("gymId").toInt()
                        )
                    } else {
                        Log.d("infoResponse faill", idResponse.errorBody()?.string().toString())
                    }
                }
                if (gymStatus != "Approved" && finalAccessToken != null){
                    val gymStatusDeferred = async { gymService.getGymStatus(finalAccessToken) }
                    val gymStatusResponse = gymStatusDeferred.await()
                    if (gymStatusResponse.isSuccessful) {
                        val gymStatus = JSONObject(gymStatusResponse.body()?.string()).getJSONObject("data").getString("status")
                        dataStore.saveGymStatus(gymStatus)
                    }else{
                        Log.d("gymStatus faill",gymStatusResponse.errorBody()?.string().toString())
                    }
                }
                withContext(Dispatchers.Main){
                    if (finalAccessToken == null){
                        val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }else if(gymStatus != "Approved") {
                        val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            }



    }
}