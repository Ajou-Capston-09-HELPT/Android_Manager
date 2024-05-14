package com.ajou.helptmanager.auth.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ajou.helptmanager.membership.view.TestMembershipActivity
import com.ajou.helptmanager.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val test = findViewById<TextView>(R.id.test)

        test.setOnClickListener {
            val intent = Intent(this, TestMembershipActivity::class.java)
            startActivity(intent)
        }
    }
}