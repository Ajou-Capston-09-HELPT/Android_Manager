package com.ajou.helptmanager.Membership

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.ajou.helptmanager.R

class ModifyMembershipDialog : DialogFragment() {

    private lateinit var etMembershipTitle: EditText
    private lateinit var etMembershipPrice: EditText
    private lateinit var tvMonthlyPrice: TextView

    private val density = Resources.getSystem().displayMetrics.density
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_modify_membership, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
}