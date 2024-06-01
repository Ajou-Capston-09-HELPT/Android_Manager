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
import com.ajou.helptmanager.home.view.dialog.ChatLinkSettingDialog
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
    private lateinit var dialog: ChatLinkSettingDialog

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
        pressHamburgerHomeButton()
        pressHamburgerMembershipButton()
        pressHamburgerQrButton()
        pressHamburgerEquipmentButton()
        pressHamburegerUserButton()
        pressHamburegerNoticeButton()
        pressHamburgerChatButton()

        return binding.root
    }

    private fun pressUploadButton() {
        binding.noticeRegisterButton.setOnClickListener {
            findNavController().navigate(R.id.action_noticeFragment_to_noticeCreateFragment)

        }
    }

    private fun pressBackButton() {
        binding.noticeToolbar.noticeBackButton.setOnClickListener {
            findNavController().popBackStack()
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

    private fun pressHamburgerHomeButton(){
        binding.noticeDrawer.home.setOnClickListener {
            binding.noticeDrawerLayout.closeDrawer(binding.noticeDrawer.drawer)
            findNavController().navigate(R.id.action_noticeFragment_to_homeFragment)
        }
    }

    private fun pressHamburgerMembershipButton(){
        binding.noticeDrawer.ticket.setOnClickListener {
            binding.noticeDrawerLayout.closeDrawer(binding.noticeDrawer.drawer)
            findNavController().navigate(R.id.action_noticeFragment_to_membershipFragment)
        }
    }

    private fun pressHamburgerQrButton(){
        binding.noticeDrawer.qr.setOnClickListener {
            binding.noticeDrawerLayout.closeDrawer(binding.noticeDrawer.drawer)
            // TODO QR 스캔
        }
    }

    private fun pressHamburgerEquipmentButton(){
        binding.noticeDrawer.train.setOnClickListener {
            binding.noticeDrawerLayout.closeDrawer(binding.noticeDrawer.drawer)
            findNavController().navigate(R.id.action_noticeFragment_to_equipmentListFragment)
        }
    }

    private fun pressHamburegerUserButton(){
        binding.noticeDrawer.user.setOnClickListener {
            binding.noticeDrawerLayout.closeDrawer(binding.noticeDrawer.drawer)
            findNavController().navigate(R.id.action_noticeFragment_to_searchUserFragment)
        }
    }

    private fun pressHamburegerNoticeButton(){
        binding.noticeDrawer.notice.setOnClickListener {
            binding.noticeDrawerLayout.closeDrawer(binding.noticeDrawer.drawer)
            findNavController().navigate(R.id.action_noticeFragment_self)
        }
    }

    private fun pressHamburgerChatButton(){
        binding.noticeDrawer.chat.setOnClickListener {
            binding.noticeDrawerLayout.closeDrawer(binding.noticeDrawer.drawer)
            dialog = ChatLinkSettingDialog()
            dialog.show(requireActivity().supportFragmentManager, "link")
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
    override fun onMoreButtonClicked(
        noticeId: Int,
        title: String,
        content: String,
        createAt: String
    ) {
        // TODO : noticeId에 해당하는 notice 수정/삭제 페이지로 이동
        Log.d("NoticeFragment", "onMoreButtonClicked: $noticeId")
        showMoreDialog(noticeId,title,content,createAt, viewModel)
    }

    override fun onDetailButtonClicked(
        noticeId: Int,
        title: String,
        content: String,
        createAt: String
    ) {
        showDetailFragment(noticeId, title, content, createAt)
    }

    private fun showMoreDialog(id: Int,title: String, content: String, createAt: String, viewModel: NoticeViewModel) {
        val dialog = NoticeMoreDialog(id, title,content,createAt, viewModel)
        dialog.show(parentFragmentManager, "MoreDialog")
    }

    private fun showDetailFragment(id: Int, title: String, content: String, createAt: String) {
        val bundle = Bundle().apply {
            putInt("noticeId", id)
            putString("title", title)
            putString("content", content)
            putString("createAt", createAt)
        }

        findNavController().navigate(R.id.action_noticeFragment_to_noticeDetailFragment, bundle)

    }

}