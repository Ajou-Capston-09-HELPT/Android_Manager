package com.ajou.helptmanager.Membership

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R

class MembershipFragment : Fragment() {
    private var membershipList: MutableList<Membership> = mutableListOf()
    private lateinit var membershipAdapter: MembershipAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_membership, container, false)
        setupRecyclerView(view)
        return view
    }

    private fun setupRecyclerView(view: View) {
        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerViewMembership)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 샘플 데이터 추가
        membershipList.add(Membership("1개월 회원권", "50,000원", "50,000원"))
        membershipList.add(Membership("2개월 회원권", "100,000원", "50,000원"))
        membershipList.add(Membership("1개월 회원권", "50,000원", "50,000원"))


        // 어댑터 초기화 및 설정
        membershipAdapter = MembershipAdapter(membershipList)
        recyclerView.adapter = membershipAdapter
    }

    // 이용권을 추가하는 메서드
    //fun addMembership(membership: Membership) {
    //    membershipList.add(membership)
    //    membershipAdapter.notifyItemInserted(membershipList.size - 1)
    //}
}