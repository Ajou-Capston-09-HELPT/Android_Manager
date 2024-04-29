package com.ajou.helptmanager

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ajou.helpt.auth.view.LogOutDialog
import com.ajou.helptmanager.auth.view.QuitDialog
import com.ajou.helptmanager.databinding.ActivityMainBinding
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.ManagerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val managerService =  RetrofitInstance.getInstance().create(ManagerService::class.java)
    private val dataStore = UserDataStore()
    private lateinit var quitDialog: QuitDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var accessToken : String? = null
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken()
            Log.d("datasore",accessToken.toString())
        }

        binding.quitBtn.setOnClickListener {
            quitDialog = QuitDialog(this)
            quitDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            quitDialog.show()
//            CoroutineScope(Dispatchers.IO).launch {
////                val refreshToken = dataStore.getRefreshToken()
//                dataStore.deleteAll()
//                Log.d("탈퇴하기",accessToken.toString())
//                val quitDeferred = async { managerService.quit(accessToken!!) }
//                val quitResponse = quitDeferred.await()
//                if (quitResponse.isSuccessful){
//                    Log.d("탈퇴하기 성공","탈퇴하기")
//                }else{
//                    Log.d("탈퇴하기 실패",quitResponse.errorBody()?.string().toString())
//                }
//            }
        }
        binding.drawer.closeBtn.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
        }
        binding.drawer.ticket.setOnClickListener {
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
            // TODO 메인화면으로 이동
        }

    }
}