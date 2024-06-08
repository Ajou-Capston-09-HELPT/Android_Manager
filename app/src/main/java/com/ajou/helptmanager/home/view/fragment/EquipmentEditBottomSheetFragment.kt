package com.ajou.helptmanager.home.view.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ajou.helptmanager.R
import com.ajou.helptmanager.databinding.FragmentEquipmentEditBottomSheetBinding
import com.ajou.helptmanager.home.model.UserInfo
import com.ajou.helptmanager.home.view.dialog.TrainSettingDialog
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.ajou.helptmanager.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EquipmentEditBottomSheetFragment : BottomSheetDialogFragment() {
    private var mContext : Context? = null
    private var _binding : FragmentEquipmentEditBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingDialog : TrainSettingDialog
    private lateinit var viewModel : UserInfoViewModel

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
        _binding = FragmentEquipmentEditBottomSheetBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(mContext!!,R.style.CustomDialog)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            // 다이얼로그 크기 설정 (인자값 : DialogInterface)
            setupRatio(bottomSheetDialog)
            dialog.setCanceledOnTouchOutside(true)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edit.setOnSingleClickListener {
            settingDialog = TrainSettingDialog()
            settingDialog.show(requireActivity().supportFragmentManager, "setting")
            dialog?.hide()

        }

        binding.delete.setOnSingleClickListener {
            val value = listOf<Int>(-1,-1,-1)
            viewModel.setEquipmentSetting(value)
            dismiss()
//            callback(value)
        }
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 20 / 100
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::settingDialog.isInitialized) settingDialog.dismiss()
    }
}