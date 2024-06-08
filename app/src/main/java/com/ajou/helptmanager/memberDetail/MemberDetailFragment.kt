package com.ajou.helptmanager.memberDetail

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.home.model.RegisteredUserInfo
import com.ajou.helptmanager.home.model.UserInfo
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.MemberService
import com.ajou.helptmanager.network.api.MembershipControllerService
import com.ajou.helptmanager.network.model.MembershipExtensionResponse
import com.ajou.helptmanager.setOnSingleClickListener
import com.bumptech.glide.Glide
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MemberDetailFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private val membershipService = RetrofitInstance.getInstance().create(MembershipControllerService::class.java)

    private var mContext : Context? = null
    private val dataStore = UserDataStore()

    private var memberId: Int? = null //선택한 유저 ID
    private var membershipId: Int = 0 //선택한 유저의 membership ID
    private lateinit var viewModel : UserInfoViewModel

    private var selectedDateString: String = ""
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setCheck(true)
                viewModel.setRegisteredUserInfo(null)
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_member_detail, container, false)
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]

        val showExerciseRecord: ConstraintLayout = view.findViewById(R.id.btnShowExerciseRecord)
        val editMembershipPeriod: ImageView = view.findViewById(R.id.ivEditMembershipPeriod)
        val backButton: ImageView = view.findViewById(R.id.memberDetailBackButton)

        viewModel.registeredUserInfo.observe(viewLifecycleOwner) { info ->
            if (info != null) updateMemberInfoUI(view, info)
        }

        showMemberDetailExerciseRecord(showExerciseRecord)

        editMemberDetailMembershipPeriod(editMembershipPeriod)

        btnMemberDetailBack(backButton)

        return view
    }

    private fun btnMemberDetailBack(backButton: ImageView) {
        backButton.setOnSingleClickListener {
            viewModel.setCheck(true)
            viewModel.setRegisteredUserInfo(null)
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
        editMembershipPeriod.setOnSingleClickListener {
            editMembershipPeriod.alpha = 0.5f
            editMembershipPeriod.postDelayed({
                editMembershipPeriod.alpha = 1.0f
            }, 100)
            editMemberMembershipPeriod()
        }
    }

    private fun showMemberDetailExerciseRecord(showExerciseRecord: ConstraintLayout) {
        showExerciseRecord.setOnSingleClickListener {
            showExerciseRecord.alpha = 0.5f
            showExerciseRecord.postDelayed({
                showExerciseRecord.alpha = 1.0f
            }, 100)
            showMemberExerciseRecord(membershipId)
        }
    }

    private fun updateMemberInfoUI(view: View, memberInfo: RegisteredUserInfo) {
        val tvMemberDetailName: TextView = view.findViewById(R.id.tvMemberDetailNameContent)
        val tvMemberDetailGender: TextView = view.findViewById(R.id.tvMemberDetailGenderContent)
        val tvMemberDetailHeight: TextView = view.findViewById(R.id.tvMemberDetailHeight)
        val tvMemberDetailWeight: TextView = view.findViewById(R.id.tvMemberDetailWeight)
        val tvMemberDetailMembershipPeriod: TextView = view.findViewById(R.id.tvMemberDetailMembershipPeriod)
        val birth : TextView = view.findViewById(R.id.birth)
        val memberDetailImage : CircleImageView = view.findViewById(R.id.memberDetailImage)

        tvMemberDetailName.text = memberInfo.userName
        when(memberInfo.gender){
            "MAN" -> tvMemberDetailGender.text = "남성"
            "WOMEN" -> tvMemberDetailGender.text = "여성"
        }
        tvMemberDetailHeight.text = String.format(getString(R.string.stringMemberDetailCm),memberInfo.height)
        tvMemberDetailWeight.text = String.format(getString(R.string.stringMemberDetailKg),memberInfo.weight)
        tvMemberDetailMembershipPeriod.text = "${memberInfo.startDate} ~ ${memberInfo.endDate}"
        birth.text = memberInfo.birthDate
        Glide.with(mContext!!)
            .load(memberInfo.profileImage)
            .centerCrop()
            .into(memberDetailImage)

    }

    private fun showMemberExerciseRecord(membershipId: Int){
        val bundle = Bundle().apply {
            putInt("memberId", viewModel.registeredUserInfo.value!!.userId)
            putString("memberName", viewModel.registeredUserInfo.value!!.userName)
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
                    val responseBody = extendMembershipResponse.body()?.string()

                    val responseBodyJson = JSONObject(responseBody)
                    val dataJson = responseBodyJson.getJSONObject("data")
                    val startDate = dataJson.getString("startDate")
                    val endDate = dataJson.getString("endDate")

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