package com.ajou.helptmanager.auth.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Button
import androidx.annotation.NonNull
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.ManagerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class QuitDialog(@NonNull context : Context) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_quit)

        val stayBtn = findViewById<Button>(R.id.stayBtn)
        val signOutBtn = findViewById<Button>(R.id.exitBtn)
        val memberService = RetrofitInstance.getInstance().create(ManagerService::class.java)
        val dataStore = UserDataStore()
        var accessToken: String? = null
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken()
        }
        signOutBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("탈퇴하기",accessToken.toString())
                val quitDeferred = async { memberService.quit(accessToken!!) }
                val quitResponse = quitDeferred.await()
                if (quitResponse.isSuccessful){
                    Log.d("탈퇴하기 성공","탈퇴하기")
                    UserDataStore().deleteAll() // 유저 데이터 삭제
                }else{
                    Log.d("탈퇴하기 실패",quitResponse.errorBody()?.string().toString())
                }
            }
            val intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            dismiss()
        }

        stayBtn.setOnClickListener {
            dismiss()
        }

    }
}