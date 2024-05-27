package com.ajou.helptmanager.home.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.home.view.HomeActivity
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentHomeBinding
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.QrService
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.*

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null
    private val dataStore = UserDataStore()
    private var userName : String? = null
    private val qrService = RetrofitInstance.getInstance().create(QrService::class.java)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            userName = dataStore.getUserName()
            Log.d("gymId is",dataStore.getGymId().toString())
            withContext(Dispatchers.Main) {
                binding.greetMsg.text = String.format(mContext!!.resources.getString(R.string.home_greet_msg),userName)
                Log.d("kakaoid",dataStore.getKakaoId().toString())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hamburger.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.drawer.drawer)
        }

        binding.drawer.closeBtn.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
        }
        binding.drawer.ticket.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_membershipFragment)
            // TODO 이용권으로 이동
        }
        binding.drawer.qr.setOnClickListener {
            qrScan()
            // TODO QR스캔으로 이동
        }
        binding.drawer.train.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_equipmentListFragment)
        }
        binding.drawer.user.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchUserFragment)
            // TODO 회원으로 이동
        }
        binding.drawer.notice.setOnClickListener {
            // TODO 공지사항으로 이동
            findNavController().navigate(R.id.action_homeFragment_to_noticeFragment)
        }
        binding.drawer.chat.setOnClickListener {
            // TODO 채팅으로 이동
        }
        binding.drawer.home.setOnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            startActivity(intent)
            // TODO 메인화면으로 이동
        }

        binding.ticketBg.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_membershipFragment)
            // TODO 이용권으로 이동
        }

        binding.noticeBg.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_noticeFragment)
        }

        binding.userBg.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchUserFragment)
        }

        binding.machineBg.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_equipmentListFragment)
        }

        binding.qrBg.setOnClickListener {
            qrScan()
        }

    }

    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Log.d("contents", result.contents)
        } else {
            Log.d("contents", result.contents)
            checkValidation(result.contents)
//            viewModel.setTrain(result.contents)
//            Log.d("contents",result.contents)
        }
    }

    private fun qrScan() {
        barcodeLauncher.launch(ScanOptions())
    }

    private fun checkValidation(contents: String){
        CoroutineScope(Dispatchers.IO).launch {
            val gymId = dataStore.getGymId()
            val qrValidDeferred = async { qrService.validateQr(contents,gymId!!) }
            val qrValidResponse = qrValidDeferred.await()
            if (qrValidResponse.isSuccessful){
                Log.d("qrValidResponse ",qrValidResponse.body().toString())
            }else{
                Toast.makeText(mContext, "QR스캔에 실패하였습니다.",Toast.LENGTH_SHORT).show()
                Log.d("qrValieResponse fail",qrValidResponse.errorBody()?.string().toString())
            }
        }
    }
}