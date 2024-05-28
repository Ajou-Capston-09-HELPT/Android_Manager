package com.ajou.helptmanager.notice

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
import com.ajou.helptmanager.databinding.FragmentNoticeModifyBinding
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.NoticeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NoticeModifyFragment : Fragment() {

    private var _binding: FragmentNoticeModifyBinding? = null
    private val binding get() = _binding!!

    private val noticeService = RetrofitInstance.getInstance().create(NoticeService::class.java)
    private lateinit var viewModel: NoticeViewModel
    private val dataStore = UserDataStore()
    private lateinit var accessToken: String

    private var gymId: Int? = null
    private var noticeId: Int? = null
    private var title: String? = null
    private var content: String? = null
    private var createAt: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeModifyBinding.inflate(layoutInflater, container, false)

        noticeId = arguments?.getInt("noticeId")
        title = arguments?.getString("title")
        content = arguments?.getString("content")
        createAt = arguments?.getString("createAt")

        viewModel = ViewModelProvider(this).get(NoticeViewModel::class.java)

        binding.noticeModifyTitleET.setText(title)
        binding.noticeModifyContentET.setText(content)
        binding.noticeModifyDateTV.text = createAt

        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            gymId = dataStore.getGymId()
        }

        pressModifyButton(noticeId)
        pressBackButton()

        return binding.root
    }

    private fun pressModifyButton(noticeId: Int?) {
        binding.noticeModifyToolbar.noticeModifyButtonTV.setOnClickListener {
            Log.d("NoticeModifyFragment", "noticeModifyButton Clicked")
            val title = binding.noticeModifyTitleET.text.toString()
            val content = binding.noticeModifyContentET.text.toString()
            val createAt = binding.noticeModifyDateTV.text.toString()

            if (title.isEmpty() || content.isEmpty()) {
                Log.d("NoticeModifyFragment", "Title or Content is Empty")
                Toast.makeText(context, "제목과 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                Log.d("NoticeModifyFragment", "Title and Content is not Empty")
                Toast.makeText(context, "공지사항이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                modifyNotice(noticeId, title, content, createAt)
            }
        }
    }

    private fun pressBackButton() {
        binding.noticeModifyToolbar.noticeModifyBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun modifyNotice(noticeId: Int?, title: String, content: String, dateString: String) {
        val notice = NoticeModifyRequest(gymId!!, title, content, dateString)
        CoroutineScope(Dispatchers.IO).launch {
            if (accessToken != null) {
                val response = noticeService.modifyNotice(accessToken, noticeId!!, notice)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Log.d("NoticeModifyFragment", "Notice Modify Success")
                        findNavController().popBackStack()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d("NoticeModifyFragment", "Notice Modify Failed")
                    }
                }
            }
        }
    }

}