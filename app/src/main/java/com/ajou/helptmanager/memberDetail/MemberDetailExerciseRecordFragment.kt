package com.ajou.helptmanager.memberDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.EquipmentService
import com.ajou.helptmanager.network.api.RecordService
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MemberDetailExerciseRecordFragment : Fragment() {


    private val recordService = RetrofitInstance.getInstance().create(RecordService::class.java)
    private val equipmentService = RetrofitInstance.getInstance().create(EquipmentService::class.java)

    private val dataStore = UserDataStore()

    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseRecordAdapter: ExerciseRecordAdapter
//    private var exerciseRecords: MutableList<ExerciseRecord>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_member_detail_exercise_record, container, false)
        val backButton: ImageView = view.findViewById(R.id.memberDetailExerciseRecordBackButton)
        val exerciseDate: MaterialCalendarView = view.findViewById(R.id.memberDetailExerciseRecordDate)
        val tvExerciseDate: TextView = view.findViewById(R.id.exerciseRecordDate)

        setupRecyclerView(view)
        getMemberName(view)
        getMemberId()
        tvExerciseDate.visibility = View.INVISIBLE

        clickBackButton(backButton)
        clickCalenderDate(exerciseDate, tvExerciseDate)

        // 임시 데이터 생성 (추후 API 연결)
        /*
        exerciseRecords = mutableListOf(
            ExerciseRecord("풀 업", "상체 가슴", 20, 3, "00:10:10"),
            ExerciseRecord("랫 풀 다운", "상체 등", 15, 3, "00:12:20"),
            ExerciseRecord("스쿼트", "하체 허벅지", 25, 4, "00:15:50"),
            ExerciseRecord("스쿼트", "하체 허벅지", 25, 4, "00:15:50"),
            ExerciseRecord("스쿼트", "하체 허벅지", 25, 4, "00:15:50")
        )
        */
        // 추후 삭제 예정
        return view
    }

    private fun setupRecyclerView(view: View) {
        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.rvExerciseRecords)
        recyclerView.layoutManager = LinearLayoutManager(context)

        exerciseRecordAdapter = ExerciseRecordAdapter()
        recyclerView.adapter = exerciseRecordAdapter
    }

    private fun clickCalenderDate(
        exerciseDate: MaterialCalendarView,
        tvExerciseDate: TextView
    ) {
        exerciseDate.setOnDateChangedListener { widget, date, selected ->
            val selectedDate = Calendar.getInstance().apply {
                set(date.year, date.month, date.day)
            }.time
            tvExerciseDate.visibility = View.VISIBLE
            val resourceFormattedDate = String.format(
                resources.getString(R.string.stringExerciseRecordDate),
                date.month,
                date.day
            )
            Log.d("FormattedDate", "Formatted date string: $resourceFormattedDate")
            tvExerciseDate.text = resourceFormattedDate

            CoroutineScope(Dispatchers.IO).launch {
                // 날짜를 yyyy-MM-dd 형식의 문자열로 변환
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDateCalendar = Calendar.getInstance().apply {
                    time = selectedDate
                    add(Calendar.MONTH, -1)
                }
                val adjustedDate = selectedDateCalendar.time
                val formattedDate = dateFormat.format(adjustedDate)

                val accessToken = dataStore.getAccessToken().toString()
                val memberId = arguments?.getInt("memberId")
                val getExerciseRecordDeferred = async {
                    recordService.getExerciseRecordDateByMemberId(
                        accessToken,
                        memberId,
                        formattedDate
                    )
                }
                Log.d("ExerciseRecord", "Date: $formattedDate, Member ID: $memberId, Access Token: $accessToken, Date: $selectedDate")
                val getExerciseRecordResponse = getExerciseRecordDeferred.await()

                if (getExerciseRecordResponse.isSuccessful) {
                    val exerciseRecordResponse = JSONObject(getExerciseRecordResponse.body()?.string())
                    Log.d("ExerciseRecord", "Exercise Record Response: $exerciseRecordResponse")

                    val exerciseRecords = exerciseRecordResponse.getJSONArray("data").let{ jsonArray ->
                        List(jsonArray.length()) { i ->
                            val jsonObject = jsonArray.getJSONObject(i)
                            val equipmentResponse = equipmentService.getEquipment(accessToken, jsonObject.getInt("equipmentId"))
                            val equipmentJSONObject = JSONObject(equipmentResponse.body()?.string())
                            Log.d("ExerciseRecord2", "Equipment Response: $equipmentJSONObject")
                            val equipmentName = equipmentJSONObject.getJSONObject("data").getString("equipmentName")
                            ExerciseRecord(
                                equipmentName,
                                "부위(추가예정)", // TODO Part 정보 추가
                                jsonObject.getInt("count"),
                                jsonObject.getInt("setNumber"),
                                "00:10:00" // TODO Time 정보 추가
                            )
                        }
                    }
                    withContext(Dispatchers.Main) {
                        exerciseRecordAdapter.submitList(exerciseRecords)
                    }
                } else {
                    Log.d("ExerciseRecord", getExerciseRecordResponse.errorBody().toString())
                    // TODO 실패 시 실행할 로직
                }
            }
        }
    }

    private fun clickBackButton(backButton: ImageView) {
        backButton.setOnClickListener {
            backButton.alpha = 0.5f
            backButton.postDelayed({
                backButton.alpha = 1.0f
            }, 100)
            findNavController().popBackStack()
        }
    }

    private fun getMemberId() {
        val memberId = arguments?.getInt("memberId")
        Log.d("ExerciseRecord", "Member ID: $memberId")
    }

    private fun getMemberName(view: View) {
        val tvMemberDetailExerciseRecordName: TextView =
            view.findViewById(R.id.memberDetailExerciseRecordName)
        tvMemberDetailExerciseRecordName.text = arguments?.getString("memberName")
    }
}
