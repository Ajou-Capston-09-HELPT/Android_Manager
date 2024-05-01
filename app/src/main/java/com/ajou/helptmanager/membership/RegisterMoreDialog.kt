package com.ajou.helptmanager.membership

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ajou.helptmanager.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RegisterMoreDialog() : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_register_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewClickListeners()
    }

    private fun setupViewClickListeners() {
        view?.findViewById<TextView>(R.id.btnMembershipModify)?.setOnClickListener {
            modifyMembership()
        }
    }

    private fun modifyMembership(){
        dismiss()
        val dialog = ModifyMembershipDialog()
        dialog.show(parentFragmentManager, "ModifyMembershipDialog")
    }
}