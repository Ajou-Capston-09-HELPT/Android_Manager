package com.ajou.helptmanager.notice

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentNoticeCreateBinding
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.NoticeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NoticeCreateFragment : Fragment() {

    private var _binding: FragmentNoticeCreateBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null

    private val noticeService = RetrofitInstance.getInstance().create(NoticeService::class.java)
    private lateinit var viewModel: NoticeViewModel
    private val dataStore = UserDataStore()
    private lateinit var accessToken: String
    private var gymId: Int? = null

    private val currentDate = Date()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    private val dateString = dateFormat.format(currentDate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoticeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeCreateBinding.inflate(layoutInflater, container, false)

        binding.noticeCreateDateTV.text = dateString

        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            gymId = dataStore.getGymId()
        }


        pressUploadButton()
        pressBackButton()

        return binding.root
    }

    private fun pressUploadButton() {
        binding.noticeCreateToolbar.noticeCreateButtonTV.setOnClickListener {
            Log.d("NoticeCreateFragment", "noticeCreateButton Clicked")
            val title = binding.noticeCreateTitleET.text.toString()
            val content = binding.noticeCreateContentET.text.toString()

            if(title.isEmpty() || content.isEmpty()){
                Log.d("NoticeCreateFragment", "title or content is empty")
                Toast.makeText(context, "제목과 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                Log.d("NoticeCreateFragment", "title or content is not empty")
                Toast.makeText(context, "공지사항이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                uploadNotice(title, content)
            }

        }
    }

    private fun pressBackButton() {
        binding.noticeCreateToolbar.noticeCreateBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun uploadNotice(title: String, content: String) {
        val notice = NoticeUploadRequest(gymId!!, title, content, dateString)

            CoroutineScope(Dispatchers.IO).launch {
                if (accessToken != null) {
                    val response = noticeService.uploadNotice(accessToken, notice)
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            Log.d("NoticeCreateFragment", "uploadNotice Success")
                            findNavController().popBackStack()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Log.d("NoticeCreateFragment", "uploadNotice Fail")
                        }
                    }
                }
            }
    }
}