<<<<<<<< HEAD:app/src/main/java/com/ajou/helptmanager/membership/view/RegisterMoreDialog.kt
package com.ajou.helptmanager.membership.view
========
package com.ajou.helptmanager.home.view.dialog
>>>>>>>> origin/develop:app/src/main/java/com/ajou/helptmanager/home/view/dialog/RegisterMoreDialog.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ajou.helptmanager.R
<<<<<<<< HEAD:app/src/main/java/com/ajou/helptmanager/membership/view/RegisterMoreDialog.kt
import com.ajou.helptmanager.membership.viewmodel.MembershipViewModel
========
import com.ajou.helptmanager.home.viewmodel.MembershipViewModel
>>>>>>>> origin/develop:app/src/main/java/com/ajou/helptmanager/home/view/dialog/RegisterMoreDialog.kt
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RegisterMoreDialog(private val id: Int, private val viewModel: MembershipViewModel) : BottomSheetDialogFragment() {

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
        val dialog = ModifyMembershipDialog(id)
        dialog.show(parentFragmentManager, "ModifyMembershipDialog")
    }

    private fun deleteMembership()
    {
       // viewModel.deleteMembership(id)
        //회원권 삭제 로직 추가
        dismiss()
    }

}