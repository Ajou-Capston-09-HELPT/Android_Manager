package com.ajou.helptmanager.home.view.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ajou.helptmanager.R
import com.ajou.helptmanager.databinding.DialogTrainSettingBinding
import com.ajou.helptmanager.getWindowSize
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel

class TrainSettingDialog() :
    DialogFragment() {
    private var _binding: DialogTrainSettingBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private lateinit var viewModel : UserInfoViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialog) // 배경 transparent
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val point = getWindowSize(mContext!!)
        val deviceWidth = point.x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DialogTrainSettingBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]
        isCancelable = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.set.minValue = 0
        binding.set.maxValue = 100
        binding.set.value = viewModel.equipment.value?.customSet ?: 0

        binding.set.wrapSelectorWheel = false
        binding.set.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.weight.minValue = 0
        binding.weight.maxValue = 100
        binding.weight.value = viewModel.equipment.value?.customWeight ?: 0

        binding.weight.wrapSelectorWheel = false
        binding.weight.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.count.minValue = 0
        binding.count.maxValue = 100
        binding.count.value = viewModel.equipment.value?.customCount ?: 0

        binding.count.wrapSelectorWheel = false
        binding.count.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.posBtn.setOnClickListener {
            val list = listOf<Int>(binding.weight.value, binding.count.value, binding.set.value)
            viewModel.setEquipmentSetting(list)
//            Log.d("viewmodel is called from traindialog",list.toString())
            dialog?.dismiss()
        }

        binding.closeBtn.setOnClickListener {
            dialog?.dismiss()
            viewModel.setEquipment(null)
            viewModel.setPositionNull()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setEquipment(null)
        viewModel.setPositionNull()
    }
}