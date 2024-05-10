package com.ajou.helptmanager.home.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.auth.model.Gym
import com.ajou.helptmanager.auth.view.AuthInfoViewModel
import com.ajou.helptmanager.databinding.FragmentSearchUserBinding
import com.ajou.helptmanager.home.adapter.UserListViewPagerAdapter
import com.ajou.helptmanager.home.model.UserInfo
import com.ajou.helptmanager.home.view.HomeActivity
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.user.model.User

class SearchUserFragment : Fragment(){
    private var _binding : FragmentSearchUserBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private lateinit var viewModel : UserInfoViewModel
    private val TAG = SearchUserFragment::class.java.simpleName

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
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]
        _binding = FragmentSearchUserBinding.inflate(layoutInflater, container, false)

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            if (viewModel.userInfo.value != null){
                if (viewModel.userInfo.value?.ticket == null) {
                    findNavController().navigate(R.id.action_searchUserFragment_to_addUserFragment)
                } else {
                    // TODO 회원 상세 조회로 이동
                }
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hamburger.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.drawer.drawer)
        }
        binding.drawer.ticket.setOnClickListener {
            findNavController().navigate(R.id.action_searchUserFragment_to_membershipFragment)
            // 이용권으로 이동
        }
        binding.drawer.qr.setOnClickListener {
            // TODO QR스캔으로 이동
        }
        binding.drawer.train.setOnClickListener {
            findNavController().navigate(R.id.action_searchUserFragment_to_equipmentListFragment)
        }
        binding.drawer.user.setOnClickListener {
            findNavController().navigate(R.id.action_searchUserFragment_self)
            // 회원으로 이동
        }
        binding.drawer.notice.setOnClickListener {
            // TODO 공지사항으로 이동
        }
        binding.drawer.chat.setOnClickListener {
            // TODO 채팅으로 이동
        }
        binding.drawer.home.setOnClickListener {
            findNavController().navigate(R.id.action_searchUserFragment_to_homeFragment)
            // 메인화면으로 이동
        }

        binding.drawer.userIcon.setImageResource(R.drawable.menu_user_on)

        binding.drawer.user.setTextColor(resources.getColor(R.color.primary))
        binding.drawer.user.setTypeface(binding.drawer.user.typeface, Typeface.BOLD)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("등록"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("승인대기"))

        binding.list.adapter = UserListViewPagerAdapter(requireActivity() as HomeActivity)

        TabLayoutMediator(binding.tabLayout, binding.list) { tab, position ->
            tab.text = if (position == 0) "등록" else "승인대기"
        }.attach()
    }

}