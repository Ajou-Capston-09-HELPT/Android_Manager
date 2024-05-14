package com.ajou.helptmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ajou.helptmanager.databinding.ActivityHomeBinding
import com.ajou.helptmanager.membership.view.TestMembershipActivity

class HomeActivity : AppCompatActivity() {
    private var _binding : ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.hamburger.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.drawer.drawer)
        }

        binding.drawer.closeBtn.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
        }
        binding.drawer.ticket.setOnClickListener {
            val intent = Intent(this, TestMembershipActivity::class.java)
            startActivity(intent)
            // TODO 이용권으로 이동
        }
        binding.drawer.qr.setOnClickListener {
            // TODO QR스캔으로 이동
        }
        binding.drawer.train.setOnClickListener {
            // TODO 기구로 이동
        }
        binding.drawer.user.setOnClickListener {
            // TODO 회원으로 이동
        }
        binding.drawer.notice.setOnClickListener {
            // TODO 공지사항으로 이동
        }
        binding.drawer.chat.setOnClickListener {
            // TODO 채팅으로 이동
        }
        binding.drawer.home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            // TODO 메인화면으로 이동
        }

        binding.ticketBg.setOnClickListener {
            val intent = Intent(this, TestMembershipActivity::class.java)
            startActivity(intent)
            // TODO 이용권으로 이동
        }
    }
}