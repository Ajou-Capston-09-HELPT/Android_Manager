package com.ajou.helptmanager.home.view.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.databinding.DialogTrainSettingBinding
import com.ajou.helptmanager.getWindowSize

class TrainSettingDialog(val setting: List<Int>, private val callback: (List<Int>) -> Unit) :
    DialogFragment() {
    private var _binding: DialogTrainSettingBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null

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
        isCancelable = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.set.minValue = 1
        binding.set.maxValue = 2010
        binding.set.value = setting[0]

        binding.set.wrapSelectorWheel = false
        binding.set.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.weight.minValue = 1
        binding.weight.maxValue = 60
        binding.weight.value = setting[1]

        binding.weight.wrapSelectorWheel = false
        binding.weight.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.count.minValue = 1
        binding.count.maxValue = 100
        binding.count.value = setting[2]

        binding.count.wrapSelectorWheel = false
        binding.count.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.posBtn.setOnClickListener {
            val list = listOf<Int>(binding.weight.value, binding.count.value, binding.set.value)
            callback(list)
            dialog?.dismiss()
        }

        binding.closeBtn.setOnClickListener {
            dialog?.dismiss()
        }
    }
}