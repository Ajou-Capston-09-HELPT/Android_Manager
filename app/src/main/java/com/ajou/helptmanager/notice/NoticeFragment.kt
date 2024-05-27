package com.ajou.helptmanager.notice

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentNoticeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoticeFragment : Fragment(), NoticeAdapter.OnItemClickListener {

    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null

    private lateinit var viewModel: NoticeViewModel
    private val dataStore = UserDataStore()
    private lateinit var accessToken: String
    private var gymId: Int? = null

    private lateinit var noticeAdapter: NoticeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoticeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoticeBinding.inflate(layoutInflater, container, false)

        setupRecyclerView(binding.root)
        observeViewModel()

        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            gymId = dataStore.getGymId()
            viewModel.getNoticeList(accessToken, gymId!!)
        }

        binding.noticeRegisterButton.setOnClickListener {
            // TODO : 공지사항 등록 프래그먼트 이동
            Log.d("NoticeFragment", "noticeRegisterButton Clicked")
        }

        pressUploadButton()


        pressBackButton()
        pressHamburgerButton()
        pressHamburgerCloseButton()

        return binding.root
    }

    private fun pressUploadButton() {
        binding.noticeRegisterButton.setOnClickListener {
            // TODO : 공지사항 등록 프래그먼트 이동
            Log.d("NoticeFragment", "noticeRegisterButton Clicked")
            findNavController().navigate(R.id.action_noticeFragment_to_noticeCreateFragment)

        }
    }

    private fun pressBackButton() {
        binding.noticeToolbar.noticeBackButton.setOnClickListener {
            // TODO : 뒤로가기 버튼 클릭 시 이전 화면으로 이동
            Log.d("NoticeFragment", "backButton Clicked")
        }
    }

    private fun pressHamburgerButton(){
        binding.noticeToolbar.noticeHamburgerButton.setOnClickListener {
            binding.noticeDrawerLayout.openDrawer(binding.noticeDrawer.drawer)
        }
    }

    private fun pressHamburgerCloseButton(){
        binding.noticeDrawer.closeBtn.setOnClickListener {
            binding.noticeDrawerLayout.closeDrawer(binding.noticeDrawer.drawer)
        }
    }


    private fun setupRecyclerView(view: View) {
        recyclerView = binding.recyclerViewNotice
        recyclerView.layoutManager = LinearLayoutManager(context)

        noticeAdapter = NoticeAdapter(this)
        recyclerView.adapter = noticeAdapter

    }
    private fun observeViewModel() {
        viewModel.noticeList.observe(viewLifecycleOwner) {
            noticeAdapter.submitList(it)
        }
    }
    override fun onMoreButtonClicked(noticeId: Int) {
        // TODO : noticeId에 해당하는 notice 수정/삭제 페이지로 이동
        Log.d("NoticeFragment", "onMoreButtonClicked: $noticeId")
        showMoreDialog(noticeId, viewModel)
    }

    private fun showMoreDialog(id: Int, viewModel: NoticeViewModel) {
        val dialog = NoticeMoreDialog(id, viewModel)
        dialog.show(parentFragmentManager, "MoreDialog")
    }

}