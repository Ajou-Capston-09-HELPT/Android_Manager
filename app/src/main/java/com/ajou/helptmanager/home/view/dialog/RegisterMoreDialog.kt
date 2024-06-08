package com.ajou.helptmanager.home.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.home.viewmodel.MembershipViewModel
import com.ajou.helptmanager.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterMoreDialog(private val productId: Int, private val viewModel: MembershipViewModel) : BottomSheetDialogFragment() {

    private val dataStore = UserDataStore()
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
        view?.findViewById<TextView>(R.id.btnMembershipModify)?.setOnSingleClickListener {
            modifyMembership()
        }
    }
    private fun deleteClick() {
        view?.findViewById<TextView>(R.id.btnMembershipDelete)?.setOnSingleClickListener {
            deleteMembership()
        }
    }
    private fun modifyMembership(){
        dismiss()
        val dialog = ModifyMembershipDialog(productId, viewModel)
        dialog.show(parentFragmentManager, "ModifyMembershipDialog")

        dialog.dialog?.setOnShowListener{
            val window = dialog.dialog?.window
            window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }
    private fun deleteMembership() {
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            gymId = dataStore.getGymId()
            viewModel.removeProduct(accessToken, gymId, productId)
        }
        dismiss()
    }

}