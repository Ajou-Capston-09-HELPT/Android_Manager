package com.ajou.helptmanager.home.view.dialog

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.home.model.product.ProductRequest
import com.ajou.helptmanager.home.viewmodel.MembershipViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModifyMembershipDialog(
    private val productId: Int,
    private val viewModel: MembershipViewModel
    ) : DialogFragment() {

    private val dataStroe = UserDataStore()
    private lateinit var accessToken: String
    private var gymId: Int? = null

    private lateinit var etMembershipTitle: EditText
    private lateinit var etMembershipPrice: EditText
    private lateinit var tvMonthlyPrice: TextView

    private val density = Resources.getSystem().displayMetrics.density
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_modify_membership, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etMembershipTitle = view.findViewById(R.id.etModifyMembershipTitle)
        etMembershipPrice = view.findViewById(R.id.etModifyMembershipPrice)
        tvMonthlyPrice = view.findViewById(R.id.tvMonthlyPrice)

        etMembershipTitle.addTextChangedListener(titleTextWatcher)
        etMembershipPrice.addTextChangedListener(priceTextWatcher)

        val modifyMembership = view.findViewById<ConstraintLayout>(R.id.modifyMembershipRegisterButton)
        modifyMembership.setOnClickListener{
            modifyMembership()
        }

        val btnModifyClose = view.findViewById<ImageButton>(R.id.btnModifyClose)
        btnModifyClose.setOnClickListener{
            dismiss()
        }
    }
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width = (388 * density).toInt()
            val height = (294 * density).toInt()
            dialog.window?.setLayout(width, height)
        }
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

    private fun modifyMembership() {
        val membershipTitle = etMembershipTitle.text.toString().trim()
        val membershipPrice = etMembershipPrice.text.toString().trim().replace(",".toRegex(), "").toIntOrNull()
        if (membershipTitle.isNotEmpty() && membershipPrice != null) {
            CoroutineScope(Dispatchers.IO).launch {
                accessToken = dataStroe.getAccessToken().toString()
                gymId = dataStroe.getGymId()
                viewModel.modifyProduct(accessToken, gymId, productId, ProductRequest(membershipTitle.toInt(), membershipPrice.toInt()))
            }
            dismiss()
        } else {
            // 입력값 오류 처리예정
            dismiss()
        }
    }

}