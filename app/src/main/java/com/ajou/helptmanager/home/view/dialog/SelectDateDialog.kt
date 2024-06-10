package com.ajou.helptmanager.home.view.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.databinding.DialogSelectDateBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class SelectDateDialog(private val callback: (String) -> Unit) : DialogFragment() {
    private lateinit var binding: DialogSelectDateBinding
    private var mContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialog) // 배경 transparent
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSelectDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val today = LocalDate.now()

        binding.year.minValue = 1900
        binding.year.maxValue = today.year
        binding.year.value = today.year

        binding.month.minValue = 1
        binding.month.maxValue = 12
        binding.month.value = today.monthValue

        binding.day.minValue = 1
        binding.day.maxValue = 31
        binding.day.value = today.dayOfMonth

        binding.posBtn.setOnClickListener {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val data = format.format(
                java.sql.Date.valueOf(
                    LocalDate.of(
                        binding.year.value,
                        binding.month.value,
                        binding.day.value
                    ).toString() // LocalDate -> Date -> String
                )
            )
            callback(data)
            dialog?.dismiss()
        }

        binding.negBtn.setOnClickListener {
            dialog?.dismiss()
        }
    }
}