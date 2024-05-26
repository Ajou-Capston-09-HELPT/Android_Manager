package com.ajou.helptmanager.home.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentMembershipBinding
import com.ajou.helptmanager.home.adapter.MembershipAdapter
import com.ajou.helptmanager.home.viewmodel.MembershipViewModel
import com.ajou.helptmanager.home.view.dialog.RegisterMembershipDialog
import com.ajou.helptmanager.home.view.dialog.RegisterMoreDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MembershipFragment : Fragment(), MembershipAdapter.OnItemClickListener {
    private var _binding: FragmentMembershipBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null

    private lateinit var viewModel: MembershipViewModel
    private val dataStroe = UserDataStore()
    private lateinit var accessToken: String
    private var gymId: Int? = null

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
            accessToken = dataStroe.getAccessToken().toString()
            gymId = dataStroe.getGymId()
            viewModel.getProductList(accessToken, gymId)
        }

        // 버튼 초기화
        binding.membershipRegisterButton.setOnClickListener {
            showRegisterMembershipDialog()
        }

        pressBackButton()
        pressHamburgerButton()
        pressHamburgerCloseButton()

        binding.drawer.ticket.setOnClickListener {
            //findNavController().navigate(R.id.action_membershipFragment_to_membershipFragment)
        }
        return binding.root
    }

    private fun pressHamburgerCloseButton() {
        binding.drawer.closeBtn.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
        }
    }

    private fun pressHamburgerButton() {
        binding.toolbar.membershipHamburgerButton.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.drawer.drawer)
        }
    }

    private fun pressBackButton() {
        binding.toolbar.membershipBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showRegisterMembershipDialog(){
        val dialog = RegisterMembershipDialog(viewModel)
        dialog.show(parentFragmentManager, "RegisterMembershipDialog")
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