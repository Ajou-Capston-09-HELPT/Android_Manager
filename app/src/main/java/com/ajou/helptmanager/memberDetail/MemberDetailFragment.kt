package com.ajou.helptmanager.memberDetail

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.MemberService
import com.ajou.helptmanager.network.api.MembershipControllerService
import com.ajou.helptmanager.network.model.MembershipExtensionResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MemberDetailFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private val memberService = RetrofitInstance.getInstance().create(MemberService::class.java)
    private val membershipService = RetrofitInstance.getInstance().create(MembershipControllerService::class.java)

    private val dataStore = UserDataStore()


    private val memberId: Int = 1 //임시 데이터 ==> 선택한 유저 ID 불러오는 로직 추가
    private var membershipId: Int = 0 //임시 데이터 ==> 선택한 유저의 membership ID 불러오는 로직 추가

    private var selectedDateString: String = ""

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

        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = dataStore.getAccessToken()

            val memberId = 1 //임시 데이터 => 선택한 유저의 ID 불러오는 로직 추가

            if (accessToken != null) {
                val memberInfoDeferred =
                    async { memberService.getMemberInfo(accessToken, memberId.toString()) }
                val memberInfoResponse = memberInfoDeferred.await()
                if (memberInfoResponse.isSuccessful) {
                    val memberInfoBody = JSONObject(memberInfoResponse.body()?.string())
                    val memberInfo = MemberDetail(
                        memberInfoBody.getJSONObject("data").getString("userName"),
                        memberInfoBody.getJSONObject("data").getString("gender"),
                        memberInfoBody.getJSONObject("data").getString("height"),
                        memberInfoBody.getJSONObject("data").getString("weight"),
                        memberInfoBody.getJSONObject("data").getString("startDate"),
                        memberInfoBody.getJSONObject("data").getString("endDate")
                    )
                    membershipId = memberInfoBody.getJSONObject("data").getInt("membershipId")

                    withContext(Dispatchers.Main) {
                        updateMemberInfoUI(view, memberInfo)
                    }
                }
            }
        }

        showMemberDetailExerciseRecord(showExerciseRecord)

        editMemberDetailMembershipPeriod(editMembershipPeriod)

        btnMemberDetailBack(backButton)

        return view
    }

    private fun btnMemberDetailBack(backButton: ImageView) {
        backButton.setOnClickListener {
            backButton.alpha = 0.5f
            backButton.postDelayed({
                backButton.alpha = 1.0f
            }, 100)
            val navController = findNavController()
            if (navController.previousBackStackEntry != null) {
                navController.popBackStack()
            }
        }
    }

    private fun editMemberDetailMembershipPeriod(editMembershipPeriod: ImageView) {
        editMembershipPeriod.setOnClickListener {
            editMembershipPeriod.alpha = 0.5f
            editMembershipPeriod.postDelayed({
                editMembershipPeriod.alpha = 1.0f
            }, 100)
            editMemberMembershipPeriod()
        }
    }

    private fun showMemberDetailExerciseRecord(showExerciseRecord: ConstraintLayout) {
        showExerciseRecord.setOnClickListener {
            showExerciseRecord.alpha = 0.5f
            showExerciseRecord.postDelayed({
                showExerciseRecord.alpha = 1.0f
            }, 100)
            showMemberExerciseRecord(membershipId)
        }
    }

    private fun updateMemberInfoUI(view: View, memberInfo: MemberDetail) {
        val tvMemberDetailName: TextView = view.findViewById(R.id.tvMemberDetailNameContent)
        val tvMemberDetailGender: TextView = view.findViewById(R.id.tvMemberDetailGenderContent)
        val tvMemberDetailHeight: TextView = view.findViewById(R.id.tvMemberDetailHeight)
        val tvMemberDetailWeight: TextView = view.findViewById(R.id.tvMemberDetailWeight)
        val tvMemberDetailMembershipPeriod: TextView = view.findViewById(R.id.tvMemberDetailMembershipPeriod)

        tvMemberDetailName.text = memberInfo.userName
        tvMemberDetailGender.text = memberInfo.gender
        tvMemberDetailHeight.text = memberInfo.height
        tvMemberDetailWeight.text = memberInfo.weight
        tvMemberDetailMembershipPeriod.text = "${memberInfo.startDate} ~ ${memberInfo.endDate}"
    }

    private fun showMemberExerciseRecord(membershipId: Int){
        val bundle = Bundle().apply {
            putInt("memberId", memberId)
        }
        findNavController().navigate(R.id.action_memberDetailFragment_to_memberDetailExerciseRecordFragment, bundle)
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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        selectedDateString = dateFormat.format(selectedDate)

        extendMembershipPeriod(selectedDateString)
    }
    private fun extendMembershipPeriod(endDate: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = dataStore.getAccessToken()



            if (accessToken != null) {
                val extendMembershipDeferred =
                    async { membershipService.extendMembership(accessToken, memberId, endDate) }
                val extendMembershipResponse = extendMembershipDeferred.await()
                if (extendMembershipResponse.isSuccessful) {
                    val responseBody = extendMembershipResponse.body()?.string()
                    val responseBodyJson = JSONObject(responseBody)
                    val dataJson = responseBodyJson.getJSONObject("data")
                    val startDate = dataJson.getString("startDate")
                    val endDate = dataJson.getString("endDate")
                    Log.d("ExtendMembership", "Response body: $responseBody")


                    withContext(Dispatchers.Main) {
                        val tvMemberDetailMembershipPeriod: TextView = view?.findViewById(R.id.tvMemberDetailMembershipPeriod)!!
                        tvMemberDetailMembershipPeriod.text = "$startDate ~ $endDate"
                        Log.d("ExtendMembership", "Membership extended successfully")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d("ExtendMembership", "Failed to extend membership. HTTP status code: ${extendMembershipResponse.code()}, accessToken:$accessToken, membershipId:$membershipId, endDate:$endDate ${extendMembershipResponse.errorBody()?.string()}")
                    }
                }
            }
        }
    }
}