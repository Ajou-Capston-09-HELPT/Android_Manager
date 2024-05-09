package com.ajou.helptmanager.memberDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import com.ajou.helptmanager.R


class MemberDetailFragment : Fragment() {

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
        val genderSpinner: Spinner = view.findViewById(R.id.genderSpinner)

        val genderSpinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_array,
            R.layout.spinner_gender_item
        )
        genderSpinnerAdapter.setDropDownViewResource(R.layout.spinner_gender_dropdown_item)
        genderSpinner.adapter = genderSpinnerAdapter


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

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGender = parent?.getItemAtPosition(position).toString()
                // TODO 선택된 성별에 따른 처리 로직 구현
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO 아무것도 안골랐을때
            }
        }

        return view
    }


    private fun showMemberExerciseRecord(){
        // TODO 운동기록 보기
    }

    private fun showMemberGenderList(){
        //TODO 멤버 성별 고르기
    }

    private fun editMemberMembershipPeriod(){
        //TODO 회원권 기간 수정
    }
}