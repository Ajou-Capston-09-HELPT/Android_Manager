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
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.auth.model.Gym
import com.ajou.helptmanager.auth.view.AuthInfoViewModel
import com.ajou.helptmanager.databinding.FragmentSearchUserBinding
import com.ajou.helptmanager.home.adapter.UserListViewPagerAdapter
import com.ajou.helptmanager.home.model.UserInfo
import com.ajou.helptmanager.home.view.HomeActivity
import com.ajou.helptmanager.home.view.dialog.ChatLinkSettingDialog
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.user.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchUserFragment : Fragment(){
    private var _binding : FragmentSearchUserBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private lateinit var viewModel : UserInfoViewModel
    private val TAG = SearchUserFragment::class.java.simpleName
    private lateinit var dialog : ChatLinkSettingDialog
    private var userName : String? = null
    private val dataStore = UserDataStore()

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

        CoroutineScope(Dispatchers.IO).launch {
            userName = dataStore.getUserName()
            withContext(Dispatchers.Main){
                binding.drawer.name.text = userName
            }
        }
        viewModel.userId.observe(viewLifecycleOwner, Observer {
            if (viewModel.check.value == null || viewModel.check.value == false){
                if (viewModel.admissionId.value == null) {
                    val bundle = Bundle().apply {
                        putInt("userId", viewModel.userId.value!!)
                    }

                    findNavController().navigate(R.id.action_searchUserFragment_to_memberDetailFragment, bundle)
                } else {
                    findNavController().navigate(R.id.action_searchUserFragment_to_addUserFragment)
                }
            }else{
                viewModel.setCheck(false)
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.hamburger.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.drawer.drawer)
        }
        binding.drawer.closeBtn.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
        }
        binding.drawer.ticket.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_searchUserFragment_to_membershipFragment)
        }
        binding.drawer.qr.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_searchUserFragment_to_homeFragment)
        }
        binding.drawer.train.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_searchUserFragment_to_equipmentListFragment)
        }
        binding.drawer.user.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_searchUserFragment_self)
        }
        binding.drawer.notice.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_searchUserFragment_to_noticeFragment)
        }
        binding.drawer.chat.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            dialog = ChatLinkSettingDialog()
            dialog.show(requireActivity().supportFragmentManager, "chatLinkSetting")
        }
        binding.drawer.home.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_searchUserFragment_to_homeFragment)
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