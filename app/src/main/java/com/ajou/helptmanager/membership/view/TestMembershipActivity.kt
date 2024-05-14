package com.ajou.helptmanager.membership.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.ajou.helptmanager.R
import com.ajou.helptmanager.membership.view.MembershipFragment

class TestMembershipActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // MembershipFragment를 추가
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, MembershipFragment())
            }
        }
    }
}