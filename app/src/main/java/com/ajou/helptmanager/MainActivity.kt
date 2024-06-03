package com.ajou.helptmanager

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}