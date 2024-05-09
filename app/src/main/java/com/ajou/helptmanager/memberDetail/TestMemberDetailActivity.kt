package com.ajou.helptmanager.memberDetail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.ajou.helptmanager.R
import com.ajou.helptmanager.databinding.ActivityMainBinding
import com.ajou.helptmanager.membership.MembershipFragment

class TestMemberDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_member_detail)
    }
}