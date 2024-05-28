package com.ajou.helptmanager.memberDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.findNavController
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
                    recordService.getExerciseRecord(
                        accessToken,
                        memberId,
                        formattedDate
                    )
                }

                Log.d(
                    "ExerciseRecord",
                    "Date: $formattedDate, Member ID: $memberId, Access Token: $accessToken, Date: $selectedDate"
                )

                val getExerciseRecordResponse = getExerciseRecordDeferred.await()




                Log.d("ExerciseRecord", "Response: $getExerciseRecordResponse , ${getExerciseRecordResponse.errorBody()?.string()} ")

                if (getExerciseRecordResponse.isSuccessful) {
                    val exerciseRecordResponse =
                        JSONObject(getExerciseRecordResponse.body()?.string())

                    Log.d("ExerciseRecord", "Exercise Record Response: ${exerciseRecordResponse}")
                    //  TODO 수정 예정
                    if (!exerciseRecordResponse.isNull("data")) {
                        val jsonArray = exerciseRecordResponse.getJSONArray("data")
                        val exerciseRecords = if (jsonArray != null) {
                            List(jsonArray.length()) { i ->
                                val jsonObject = jsonArray.getJSONObject(i)
                                val equipmentResponse = equipmentService.getEquipment(accessToken, jsonObject.getInt("equipmentId"))
                                val equipmentResponseBody = equipmentResponse.body()?.string()
                                val equipmentName = if (equipmentResponseBody != null) {
                                    val equipmentJSONObject = JSONObject(equipmentResponseBody)
                                    Log.d("ExerciseRecord2", "Equipment Response: $equipmentJSONObject")
                                    equipmentJSONObject.getJSONObject("data").getString("equipmentName")
                                } else {
                                    "장비 이름 없음" // 또는 적절한 기본값 설정
                                }

                                val startTime = jsonObject.getString("startTime")
                                val endTime = jsonObject.getString("endTime")

                                val duration = calculateDuration(startTime, endTime)


                                ExerciseRecord(
                                    equipmentName,
                                    jsonObject.getInt("count"),
                                    jsonObject.getInt("setNumber"),
                                    duration
                                )
                            }
                        } else {
                            emptyList()
                        }
                        withContext(Dispatchers.Main) {
                            exerciseRecordAdapter.submitList(exerciseRecords)
                        }
                    } else {
                        Log.d("ExerciseRecord", "No data in response")
                        // TODO 'data'가 없는 경우에 대한 처리
                    }
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

    private fun calculateDuration(startTime: String, endTime: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val startDate = dateFormat.parse(startTime)
        val endDate = dateFormat.parse(endTime)

        val durationMillis = endDate.time - startDate.time
        val seconds = (durationMillis / 1000) % 60
        val minutes = (durationMillis / (1000 * 60)) % 60
        val hours = (durationMillis / (1000 * 60 * 60)) % 24

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}