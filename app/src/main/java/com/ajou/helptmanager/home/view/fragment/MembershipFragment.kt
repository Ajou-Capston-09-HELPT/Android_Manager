package com.ajou.helptmanager.home.view.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentMembershipBinding
import com.ajou.helptmanager.home.adapter.MembershipAdapter
import com.ajou.helptmanager.home.view.dialog.ChatLinkSettingDialog
import com.ajou.helptmanager.home.viewmodel.MembershipViewModel
import com.ajou.helptmanager.home.view.dialog.RegisterMembershipDialog
import com.ajou.helptmanager.home.view.dialog.RegisterMoreDialog
import com.ajou.helptmanager.setOnSingleClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MembershipFragment : Fragment(), MembershipAdapter.OnItemClickListener {
    private var _binding: FragmentMembershipBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null

    private lateinit var viewModel: MembershipViewModel
    private val dataStore = UserDataStore()
    private lateinit var accessToken: String
    private var gymId: Int? = null
    private var userName : String? = null
    private lateinit var dialog: ChatLinkSettingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MembershipViewModel::class.java)
    }
    private lateinit var membershipAdapter: MembershipAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMembershipBinding.inflate(layoutInflater, container, false)
        setupRecyclerView(binding.root)
        observeViewModel()

        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            gymId = dataStore.getGymId()
            userName = dataStore.getUserName()
            viewModel.getProductList(accessToken, gymId)
            binding.membershipDrawer.name.text = userName
        }

        // 버튼 초기화
        binding.membershipRegisterButton.setOnSingleClickListener {
            showRegisterMembershipDialog()
        }

        pressBackButton()
        pressHamburgerButton()
        pressHamburgerCloseButton()
        pressHamburgerHomeButton()
        pressHamburgerMembershipButton()
        pressHamburgerQrButton()
        pressHamburgerEquipmentButton()
        pressHamburgerUserButton()
        pressHamburgerNoticeButton()
        pressHamburgerChatButton()
        pressHamburgerEntryLogButton()
        binding.membershipDrawer.ticketIcon.setImageResource(R.drawable.menu_ticket_on)

        binding.membershipDrawer.ticketIcon.setImageResource(R.drawable.menu_ticket_on)

        binding.membershipDrawer.ticket.setTextColor(resources.getColor(R.color.primary))
        binding.membershipDrawer.ticket.setTypeface(binding.membershipDrawer.user.typeface, Typeface.BOLD)
        return binding.root
    }



    private fun pressHamburgerButton() {
        binding.membershipToolbar.membershipHamburgerButton.setOnSingleClickListener {
            binding.membershipDrawerLayout.openDrawer(binding.membershipDrawer.drawer)
        }
    }
    private fun pressHamburgerCloseButton() {
        binding.membershipDrawer.closeBtn.setOnSingleClickListener {
            binding.membershipDrawerLayout.closeDrawer(binding.membershipDrawer.drawer)
        }
    }

    private fun pressHamburgerHomeButton() {
        binding.membershipDrawer.home.setOnSingleClickListener {
            binding.membershipDrawerLayout.closeDrawer(binding.membershipDrawer.drawer)
            findNavController().navigate(R.id.action_membershipFragment_to_homeFragment)
        }
    }

    private fun pressHamburgerMembershipButton() {
        binding.membershipDrawer.ticket.setOnSingleClickListener {
            binding.membershipDrawerLayout.closeDrawer(binding.membershipDrawer.drawer)
            findNavController().navigate(R.id.action_membershipFragment_self)
        }
    }

    private fun pressHamburgerQrButton() {
        binding.membershipDrawer.qr.setOnSingleClickListener {
            binding.membershipDrawerLayout.closeDrawer(binding.membershipDrawer.drawer)
            findNavController().navigate(R.id.action_membershipFragment_to_homeFragment)
        }
    }

    private fun pressHamburgerEquipmentButton() {
        binding.membershipDrawer.train.setOnSingleClickListener {
            binding.membershipDrawerLayout.closeDrawer(binding.membershipDrawer.drawer)
            findNavController().navigate(R.id.action_membershipFragment_to_equipmentListFragment)
        }
    }

    private fun pressHamburgerUserButton() {
        binding.membershipDrawer.user.setOnSingleClickListener {
            binding.membershipDrawerLayout.closeDrawer(binding.membershipDrawer.drawer)
            findNavController().navigate(R.id.action_membershipFragment_to_searchUserFragment)
        }
    }

    private fun pressHamburgerNoticeButton() {
        binding.membershipDrawer.notice.setOnSingleClickListener {
            binding.membershipDrawerLayout.closeDrawer(binding.membershipDrawer.drawer)
            findNavController().navigate(R.id.action_membershipFragment_to_noticeFragment)
        }
    }

    private fun pressHamburgerChatButton() {
        binding.membershipDrawer.chat.setOnSingleClickListener {
            binding.membershipDrawerLayout.closeDrawer(binding.membershipDrawer.drawer)
            dialog = ChatLinkSettingDialog()
            dialog.show(requireActivity().supportFragmentManager, "chatLinkSetting")
        }
    }
    private fun pressHamburgerEntryLogButton() {
        binding.membershipDrawer.entryLog.setOnSingleClickListener {
            binding.membershipDrawerLayout.closeDrawer(binding.membershipDrawer.drawer)
            findNavController().navigate(R.id.action_membershipFragment_to_entryLogFragment)
        }
    }


    private fun pressBackButton() {
        binding.membershipToolbar.membershipBackButton.setOnSingleClickListener {
            findNavController().popBackStack()
        }
    }
    private fun showRegisterMembershipDialog() {
        val dialog = RegisterMembershipDialog(viewModel)

        dialog.show(parentFragmentManager, "RegisterMembershipDialog")

        // 다이얼로그가 보여진 후 크기를 설정합니다.
        dialog.dialog?.setOnShowListener {
            val window = dialog.dialog?.window
            window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun observeViewModel() {
        viewModel.membershipList.observe(viewLifecycleOwner) { membershipList ->
            membershipAdapter.submitList(membershipList)
        }
    }

    private fun setupRecyclerView(view: View) {
        // RecyclerView 초기화
        recyclerView = binding.recyclerViewMembership
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 어댑터 초기화 및 설정
        membershipAdapter = MembershipAdapter(this)
        recyclerView.adapter = membershipAdapter
    }

    override fun onMoreButtonClicked(productId: Int) {
        showMoreDialog(productId, viewModel)
    }

    private fun showMoreDialog(id: Int, viewModel: MembershipViewModel) {
        val dialog = RegisterMoreDialog(id, viewModel)
        dialog.show(parentFragmentManager, "RegisterMoreDialog")
    }


}