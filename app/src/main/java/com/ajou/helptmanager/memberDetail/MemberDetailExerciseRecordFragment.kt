package com.ajou.helptmanager.memberDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Calendar

class MemberDetailExerciseRecordFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseRecordAdapter: ExerciseRecordAdapter
    private lateinit var exerciseRecords: MutableList<ExerciseRecord>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_member_detail_exercise_record, container, false)

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.rvExerciseRecords)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 임시 데이터 생성 (추후 API 연결)
        exerciseRecords = mutableListOf(
            ExerciseRecord("풀 업", "상체 가슴", 20, 3, "00:10:10"),
            ExerciseRecord("랫 풀 다운", "상체 등", 15, 3, "00:12:20"),
            ExerciseRecord("스쿼트", "하체 허벅지", 25, 4, "00:15:50"),
            ExerciseRecord("스쿼트", "하체 허벅지", 25, 4, "00:15:50"),
            ExerciseRecord("스쿼트", "하체 허벅지", 25, 4, "00:15:50")
        )
        // 추후 삭제 예정

        exerciseRecordAdapter = ExerciseRecordAdapter(exerciseRecords)
        recyclerView.adapter = exerciseRecordAdapter

        val tvExerciseDate: TextView = view.findViewById(R.id.exerciseRecordDate)
        tvExerciseDate.visibility = View.INVISIBLE
        val backButton: ImageView = view.findViewById(R.id.memberDetailExerciseRecordBackButton)
        val exerciseDate: MaterialCalendarView = view.findViewById(R.id.memberDetailExerciseRecordDate)

        backButton.setOnClickListener{
            backButton.alpha = 0.5f
            backButton.postDelayed({
                backButton.alpha = 1.0f
            }, 100)
            findNavController().popBackStack()
        }

        exerciseDate.setOnDateChangedListener { widget, date, selected ->
            val selectedDate = Calendar.getInstance().apply {
                set(date.year, date.month, date.day)
            }.time
            tvExerciseDate.visibility = View.VISIBLE
            val resourceFormattedDate = String.format(resources.getString(R.string.stringExerciseRecordDate), date.month, date.day)
            Log.d("FormattedDate", "Formatted date string: $resourceFormattedDate")
            tvExerciseDate.text = resourceFormattedDate

            // TODO 날짜 변경 시 실행할 로직
        }
        return view
    }


}
