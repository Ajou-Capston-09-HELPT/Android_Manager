package com.ajou.helptmanager.home.view.dialog

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.home.model.ProductRequest
import com.ajou.helptmanager.home.viewmodel.MembershipViewModel
import com.ajou.helptmanager.setOnSingleClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterMembershipDialog(
    private val viewModel: MembershipViewModel
) : DialogFragment() {

    private val dataStore = UserDataStore()
    private lateinit var accessToken: String
    private var gymId: Int? = null

    private lateinit var etMembershipTitle: EditText
    private lateinit var etMembershipPrice: EditText
    private lateinit var tvMonthlyPrice: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_register_membership, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etMembershipTitle = view.findViewById(R.id.etRegisterMembershipTitle)
        etMembershipPrice = view.findViewById(R.id.etRegisterMembershipPrice)
        tvMonthlyPrice = view.findViewById(R.id.tvMonthlyPrice)

        etMembershipTitle.addTextChangedListener(titleTextWatcher)
        etMembershipPrice.addTextChangedListener(priceTextWatcher)

        val registerMembership = view.findViewById<ConstraintLayout>(R.id.registerMembershipRegisterButton)
        registerMembership.setOnSingleClickListener {
            registerMembership()
        }

        val btnRegisterClose = view.findViewById<ImageButton>(R.id.btnRegisterClose)
        btnRegisterClose.setOnSingleClickListener {
            dismiss() // 다이얼로그 닫기
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그의 너비를 화면 너비의 90%로 설정
        val width = (Resources.getSystem().displayMetrics.widthPixels * 0.9).toInt()
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    private val titleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            calculateMonthlyPrice()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private val priceTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val formatted = s.toString().replace(",".toRegex(), "").toIntOrNull()?.let {
                String.format("%,d", it)
            } ?: ""

            etMembershipPrice.removeTextChangedListener(this)
            etMembershipPrice.setText(formatted)
            etMembershipPrice.setSelection(formatted.length)
            etMembershipPrice.addTextChangedListener(this)

            calculateMonthlyPrice()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun calculateMonthlyPrice() {
        val monthsText = etMembershipTitle.text.toString().trim()
        val priceText = etMembershipPrice.text.toString().replace(",".toRegex(), "")

        if (monthsText.isNotEmpty() && priceText.isNotEmpty()) {
            val months = monthsText.toIntOrNull()
            val price = priceText.toIntOrNull()

            if (months != null && price != null && months > 0) {
                val monthlyPrice = price / months
                tvMonthlyPrice.text = resources.getString(R.string.monthly_price, monthlyPrice)
            } else {
                tvMonthlyPrice.text = ""
            }
        } else {
            tvMonthlyPrice.text = ""
        }
    }

    private fun registerMembership() {
        val membershipTitle = etMembershipTitle.text.toString().trim()
        val membershipPrice = etMembershipPrice.text.toString().trim().replace(",".toRegex(), "").toIntOrNull()

        if (membershipTitle.isNotEmpty() && membershipPrice != null) {
            val intMembershipTitle = membershipTitle.toInt()
            CoroutineScope(Dispatchers.IO).launch {
                accessToken = dataStore.getAccessToken().toString()
                gymId = dataStore.getGymId()
                viewModel.addProduct(accessToken, gymId, ProductRequest(intMembershipTitle, membershipPrice))
            }
            dismiss()
        } else {
            Log.d("RegisterMembershipDialog", "registerMembership: title or price is empty")
            dismiss()
        }
    }
}
