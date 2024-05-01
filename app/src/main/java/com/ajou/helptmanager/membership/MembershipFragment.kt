package com.ajou.helptmanager.membership

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R

class MembershipFragment : Fragment(), MembershipAdapter.OnItemClickListener {
    private lateinit var viewModel: MembershipViewModel
    private lateinit var membershipRegisterButton: Button

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_membership, container, false)
        setupRecyclerView(view)
        observeViewModel()

        // 버튼 초기화
        val membershipRegisterButton: ConstraintLayout = view.findViewById(R.id.membershipRegisterButton)
        membershipRegisterButton.setOnClickListener {
            showRegisterMembershipDialog()
        }

        return view
    }

    private fun showRegisterMembershipDialog(){
        val dialog = RegisterMembershipDialog()
        dialog.show(parentFragmentManager, "RegisterMembershipDialog")
    }
    private fun observeViewModel() {
        viewModel.membershipList.observe(viewLifecycleOwner) { membershipList ->
            membershipAdapter.submitList(membershipList)
        }
    }

    private fun setupRecyclerView(view: View) {
        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerViewMembership)
        recyclerView.layoutManager = LinearLayoutManager(context)


        // 어댑터 초기화 및 설정
        membershipAdapter = MembershipAdapter(this)
        recyclerView.adapter = membershipAdapter
    }
    override fun onMoreButtonClicked(id: Int) {
        showMoreDialog(id, viewModel)
    }


    private fun showMoreDialog(id: Int, viewModel: MembershipViewModel) {
        val dialog = RegisterMoreDialog(id, viewModel)
        dialog.show(parentFragmentManager, "RegisterMoreDialog")
    }


}