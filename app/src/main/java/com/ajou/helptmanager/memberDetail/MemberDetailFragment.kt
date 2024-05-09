package com.ajou.helptmanager.memberDetail

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.R
import java.util.Calendar


class MemberDetailFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_member_detail, container, false)

        val showExerciseRecord: ConstraintLayout = view.findViewById(R.id.btnShowExerciseRecord)
        val editMembershipPeriod: ImageView = view.findViewById(R.id.ivEditMembershipPeriod)
        val backButton: ImageView = view.findViewById(R.id.memberDetailBackButton)

        showExerciseRecord.setOnClickListener{
            showExerciseRecord.alpha = 0.5f
            showExerciseRecord.postDelayed({
                showExerciseRecord.alpha = 1.0f
            }, 100)
            showMemberExerciseRecord()
        }

        editMembershipPeriod.setOnClickListener {
            editMembershipPeriod.alpha = 0.5f
            editMembershipPeriod.postDelayed({
                editMembershipPeriod.alpha = 1.0f
            }, 100)
            editMemberMembershipPeriod()
        }

        backButton.setOnClickListener {
            backButton.alpha = 0.5f
            backButton.postDelayed({
                backButton.alpha = 1.0f
            }, 100)
            findNavController().popBackStack()
        }

        return view
    }


    private fun showMemberExerciseRecord(){
        findNavController().navigate(R.id.action_memberDetailFragment_to_memberDetailExerciseRecordFragment)
    }
    private fun editMemberMembershipPeriod(){
        //TODO 회원권 기간 수정
        showDatePicker()
    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            year, month, day
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }.time

    }
}