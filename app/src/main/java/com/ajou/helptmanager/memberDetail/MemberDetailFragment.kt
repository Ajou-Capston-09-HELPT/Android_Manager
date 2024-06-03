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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.MemberService
import com.ajou.helptmanager.network.api.MembershipControllerService
import com.bumptech.glide.Glide
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


    private var memberId: Int? = null //선택한 유저 ID
    private var membershipId: Int = 0 //선택한 유저의 membership ID

    private val _memberInfo = MutableLiveData<MemberDetail>()
    val memberInfo: LiveData<MemberDetail> = _memberInfo

    private var selectedDateString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        memberId = arguments?.getInt("userId")


        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = dataStore.getAccessToken()

            if (accessToken != null) {
                val memberInfoDeferred =
                    async { memberService.getMemberInfo(accessToken, memberId.toString()) }
                val memberInfoResponse = memberInfoDeferred.await()
                if (memberInfoResponse.isSuccessful) {
                    val memberInfoBody = JSONObject(memberInfoResponse.body()?.string())

                    val translatedGender = when (val gender = memberInfoBody.getJSONObject("data").getString("gender")){
                        "WOMEN" -> "여성"
                        "MAN" -> "남성"
                        else -> gender
                    }


                    val memberInfo = MemberDetail(
                        memberInfoBody.getJSONObject("data").getString("userName"),
                        translatedGender,
                        memberInfoBody.getJSONObject("data").getString("height"),
                        memberInfoBody.getJSONObject("data").getString("weight"),
                        memberInfoBody.getJSONObject("data").getString("startDate"),
                        memberInfoBody.getJSONObject("data").getString("endDate")
                    )

                    Log.d("MemberDetail", "Member info: $memberInfo")

                    _memberInfo.postValue(memberInfo)
                    membershipId = memberInfoBody.getJSONObject("data").getInt("membershipId")

                } else {
                    Log.d(
                        "MemberDetail",
                        "Failed to get member info. HTTP status code: ${memberInfoResponse.code()}, accessToken:$accessToken, memberId:$memberId ${
                            memberInfoResponse.errorBody()?.string()
                        }"
                    )
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_member_detail, container, false)

        val showExerciseRecord: ConstraintLayout = view.findViewById(R.id.btnShowExerciseRecord)
        val editMembershipPeriod: ImageView = view.findViewById(R.id.ivEditMembershipPeriod)
        val backButton: ImageView = view.findViewById(R.id.memberDetailBackButton)

        //TODO 프로필 이미지 변경
        val profileImage: ImageView = view.findViewById(R.id.memberDetailImage)
        //val imageUrl = "https://helpt.s3.ap-northeast-2.amazonaws.com/profileFile/e356cc50-eda4-471c-9c1b-348f7674a992_Screenshot_20240601_180342_Samsung_Internet.jpg"
        //Glide.with(this).load(imageUrl).into(profileImage)
        // TODO


        memberInfo.observe(viewLifecycleOwner) { info ->
            updateMemberInfoUI(view, info)
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
            putInt("memberId", memberId!!)
            putString("memberName", _memberInfo.value?.userName)
        }
        findNavController().navigate(R.id.action_memberDetailFragment_to_memberDetailExerciseRecordFragment, bundle)
    }
    private fun editMemberMembershipPeriod(){
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
                    async { membershipService.extendMembership(accessToken, membershipId!!, endDate) }
                val extendMembershipResponse = extendMembershipDeferred.await()
                if (extendMembershipResponse.isSuccessful) {

                    Log.d("ExtendMembership", "Membership extended successfully")

                    val responseBody = extendMembershipResponse.body()?.string()

                    Log.d("ExtendMembership", "Response body: $responseBody")

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