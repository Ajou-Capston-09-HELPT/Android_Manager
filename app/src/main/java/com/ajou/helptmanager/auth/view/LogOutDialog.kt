package com.ajou.helptmanager.auth.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.widget.Button
import androidx.annotation.NonNull
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.auth.view.AuthActivity
import com.ajou.helptmanager.setOnSingleClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogOutDialog(@NonNull context : Context) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_logout)

        val stayBtn = findViewById<Button>(R.id.stayBtn)
        val signOutBtn = findViewById<Button>(R.id.exitBtn)

        signOutBtn.setOnSingleClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                UserDataStore().deleteAll() // 유저 데이터 삭제
            }
            val intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            dismiss()
        }

        stayBtn.setOnSingleClickListener {
            dismiss()
        }

    }
}