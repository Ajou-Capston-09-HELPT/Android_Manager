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
import com.ajou.helptmanager.databinding.FragmentMemberDetailExerciseRecordBinding
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
    private var _binding : FragmentMemberDetailExerciseRecordBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentMemberDetailExerciseRecordBinding.inflate(layoutInflater, container, false)
//        val backButton: ImageView = view.findViewById(R.id.memberDetailExerciseRecordBackButton)
//        val exerciseDate: MaterialCalendarView = view.findViewById(R.id.memberDetailExerciseRecordDate)
//        val tvExerciseDate: TextView = view.findViewById(R.id.exerciseRecordDate)

        setupRecyclerView(binding.root)
        getMemberName(binding.root)
        getMemberId()
        binding.exerciseRecordDate.visibility = View.INVISIBLE

        clickBackButton(binding.memberDetailExerciseRecordBackButton)
        clickCalenderDate(binding.memberDetailExerciseRecordDate, binding.exerciseRecordDate)

        return binding.root
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
                    val recordBody = getExerciseRecordResponse.body()!!.data
                    if (recordBody.isEmpty()) {
                        withContext(Dispatchers.Main) {
                            binding.noRecordBg.visibility = View.VISIBLE
                            binding.noRecordText.visibility = View.VISIBLE
                            binding.rvExerciseRecords.visibility = View.GONE
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            binding.rvExerciseRecords.visibility = View.VISIBLE
                            exerciseRecordAdapter.submitList(recordBody)
                            binding.noRecordBg.visibility = View.GONE
                            binding.noRecordText.visibility = View.GONE
                        }
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
