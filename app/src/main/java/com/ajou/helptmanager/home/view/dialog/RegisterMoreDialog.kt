package com.ajou.helptmanager.home.view.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.home.adapter.MembershipAdapter
import com.ajou.helptmanager.home.viewmodel.MembershipViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterMoreDialog(private val position: Int, private val viewModel: MembershipViewModel) : BottomSheetDialogFragment() {

    private val dataStroe = UserDataStore()
    private lateinit var accessToken: String
    private var gymId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_register_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        modifyClick()
        deleteClick()
    }
    private fun modifyClick() {
        view?.findViewById<TextView>(R.id.btnMembershipModify)?.setOnClickListener {
            modifyMembership()
        }
    }
    private fun deleteClick() {
        view?.findViewById<TextView>(R.id.btnMembershipDelete)?.setOnClickListener {
            deleteMembership()
        }
    }
    private fun modifyMembership(){
        dismiss()
        val dialog = ModifyMembershipDialog(position, viewModel)
        dialog.show(parentFragmentManager, "ModifyMembershipDialog")
    }
    private fun deleteMembership()
    {
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStroe.getAccessToken().toString()
            gymId = dataStroe.getGymId()
            val membership = viewModel.membershipList.value?.get(position)
            viewModel.removeProduct(accessToken, gymId, membership!!.product_id)
        }
        dismiss()
    }

}