package com.ajou.helptmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ajou.helptmanager.auth.AuthActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val dataStore = UserDataStore()
        var accessToken : String ?= null
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            val intent = Intent(this@SplashActivity, AuthActivity::class.java)
            startActivity(intent)
            withContext(Dispatchers.Main){
//                if (accessToken != "null"){
//                    Log.d("Login!",accessToken.toString())
//                    val intent = Intent(this@SplashActivity, HomeActivity::class.java)
//                    startActivity(intent)
//                }else{
//                    Log.d("Login 필요!","")
//                    val intent = Intent(this@SplashActivity, AuthActivity::class.java)
//                    startActivity(intent)
//                }
            }
        }
    }
}